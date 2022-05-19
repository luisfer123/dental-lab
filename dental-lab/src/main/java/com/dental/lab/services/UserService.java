package com.dental.lab.services;

import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.dental.lab.exceptions.AuthorityNotFoundException;
import com.dental.lab.exceptions.InvalidArgumentException;
import com.dental.lab.exceptions.UserNotFoundException;
import com.dental.lab.model.entities.Authority;
import com.dental.lab.model.entities.User;
import com.dental.lab.model.enums.EAuthority;
import com.dental.lab.model.payloads.CreateUserFormPayload;
import com.dental.lab.model.payloads.EditUserPayload;
import com.dental.lab.utils.HelperMethods;

public interface UserService {
	
	/**
	 * Finds the {@linkplain User} entity with {@code User.username} equal to the 
	 * parameter {@code username}. {@code User.authorities} property is set with
	 * all the corresponding Authority entities. username is supposed to have an 
	 * unique constrain in the database.
	 * 
	 * @param username {@code username} of the {@linkplain User} entity we want to retrieve.
	 * @return User entity with {@code User.username} equal to {@code username} and 
	 * 			{@code User.authorities} property initialized.
	 * @throws UsernameNotFoundException If no {@linkplain User} with the passed {@code username}
	 * 			was found in the database.
	 */
	User findByUsernameWithAuthorities(String username) throws UsernameNotFoundException;
	
	User findById(Long id) throws UserNotFoundException;
	
	List<User> findAll();
	
	Page<User> findAll(int pageNum, int pageSize, String sortBy);
	
	/**
	 * Creates a new {@linkplain User} from the {@linkplain CreateUserFormPayload}
	 * parameter passed, adds the corresponding {@linkplain Authority}s and saves it
	 * to the database.<br>
	 * If {@code CreateuserFormPayload.roles} is empty, or it does not contain
	 * {@code ROLE_USER}, then the {@code EAuthority.ROLE_USER} is added. All registered 
	 * User must contain at least the {@code EAuthority.ROLE_USER} Authority.
	 * 
	 * @param userPayload contains the User's information to create the new {@linkplain User}
	 * to be saved.
	 * @return The saved {@linkplain User}
	 * @throws AuthorityNotFoundException If any element in the 
	 * {@code CreateuserFormPayload.roles} list does not correspond with some
	 * {@linkplain EAuthority} constant.
	 */
	User saveNewUser(CreateUserFormPayload userPayload) throws AuthorityNotFoundException;
	
	User saveEditedUser(EditUserPayload userEdited, Long userId) throws UserNotFoundException;

	/**
	 * Computes the {@linkplain LevenshteinDistance} between the parameter 
	 * {@code username} and the {@code User.username} value for each {@linkplain User}
	 * found in the database.<br>
	 * Then returns a {@linkplain List} with the {@code n} more closest 
	 * {@code User.username}s found, according to its Levenshtein distance with the parameter 
	 * {@code username}. <br>
	 * The returned list is sorted by the value of the Levenshtein Distance.<br> 
	 * The size of the returned list is not bigger than {@code maxDistance}.
	 * 
	 * @param username
	 * @param maxDistance
	 * @return
	 */
	List<String> findUsernamesWithClosestLevenshteinDistance(String username, int maxDistance);

	User findByUsername(String username) throws UserNotFoundException;
	
	User findByEmail(String email) throws UserNotFoundException;

	List<User> findByLastName(String lastName);
	
	/**
	 * Expects {@code fullLastName} to contain either one last name or two last
	 * names. In the case it contains two last names, they must be separated by 
	 * a white space. In the case it contains only one last name, it is assumed to
	 * be the first last name<br>
	 * Both last names are split calling {@link HelperMethods#splitTwoWordsString(String)} 
	 * into two different Strings and then the method {@link #findByFullLastName(String, String)}
	 * is called.
	 * 
	 * @param fullLastName
	 * @return
	 * @throws InvalidArgumentException - If the passed String is either null or blank
	 * or it contains more than two words
	 */
	public List<User> findByFullLastName(String fullLastName) throws InvalidArgumentException;

	/**
	 * Finds {@linkplain User}s which have {@code User.lastName} equal to the 
	 * parameter {@code firstLastName} and have {@code User.secondLastName} equal
	 * to the parameter {@code secondLastName}
	 * 
	 * @param firstLastName
	 * @param secondLastName
	 * @return
	 * @throws InvalidArgumentException - If both parameters are either null
	 *  or an empty String
	 */
	List<User> findByFullLastName(String firstLastName, String secondLastName) throws InvalidArgumentException;

	/**
	 * Finds the {@linkplain User}s which have Levenshtein distance smaller or equal to
	 * {@code masDistance}, according with its {@code username}.
	 * 
	 * @param username
	 * @param maxDistance
	 * @return
	 */
	List<User> findSimilarUsersByUsername(String username, int maxDistance);
	
	/**
	 * Finds similar users according to its last name using the Levenshtein distance.<br>
	 * {@code fullLastName} is split into firstLastname and secondLastname. If {@code fullLastName}
	 * only contains one word, it is assumed to be the first last name.<br>
	 * The method {@link #findSimilarUsersByFullLastName(String, String, int)} is called 
	 * with the split strings.<br>
	 * The list returned contains only the users with Levenshtein distance smaller or equal
	 * than {@code masDistance}<br>
	 * If the passed argument {@code email} is either null or blank, an empty list
	 * will be returned.
	 * 
	 * @param fullLastName
	 * @param maxDistance
	 * @return
	 */
	List<User> findSimilarUsersByFullLastName(String fullLastName, int maxDistance);
	
	/**
	 * Finds similar users according with its last name using the Levenshtein distance.<br>
	 * If only one last name is passed, it will be used to computed the distances.<br>
	 * When both last names are passed, the Levenshtein distance is computed with the
	 * full last name as a single string.<br>
	 * The list returned contains only the users with Levenshtein distance smaller or equal
	 * than {@code masDistance}<br>
	 * If the passed arguments {@code firstLastName} and {@code secondLastname} are both 
	 * either null or blank, an empty list will be returned.
	 * 
	 * @param firstLastName
	 * @param secondLastName
	 * @param maxDistance
	 * @return
	 */
	List<User> findSimilarUsersByFullLastName(String firstLastName, String secondLastName, int maxDistance);
	
	/**
	 * Finds similar {@linkplain User}s according to its email using Levenshtein.<br>
	 * The list returned contains only the users with Levenshtein distance smaller or equal
	 * than {@code masDistance}<br>
	 * If the passed argument {@code email} is either null or blank, an empty list
	 * will be returned.
	 * 
	 * @param email
	 * @param maxDistance
	 * @return
	 */
	List<User> findSimilarUsersByEmail(String email, int maxDistance);

}