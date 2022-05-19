package com.dental.lab.model.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dental.lab.model.enums.EPhoneType;

@Entity
@Table(name = "Phone")
public class Phone {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "phone_number")
	private int phoneNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "phone_type", columnDefinition = "ENUM('HOME', 'CELLPHONE', 'OFFICE')")
	private EPhoneType phoneType;
	
	@ManyToOne
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(int phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public EPhoneType getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(EPhoneType phoneType) {
		this.phoneType = phoneType;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		
		if(!(o instanceof Phone))
			return false;
		
		Phone other = (Phone) o;
		return id != null
				&& id.equals(other.id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

}
