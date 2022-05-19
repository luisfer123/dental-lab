package com.dental.lab.model.entities;

import java.util.Objects;

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

import com.dental.lab.model.enums.EAddressType;

@Entity
@Table(name = "Address")
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "number")
	private int number;
	
	@Column(name = "inner_number")
	private String intNumber;
	
	@Column(name = "street")
	private String street;
	
	@Column(name = "zip_postcode")
	private int zipPostcode;
	
	@Column(name = "city")
	private String city;
	
	@Column(name = "address_type", columnDefinition = "ENUM('HOME', 'DENTIST_OFFICE', 'LABORATORY', 'OTHER')")
	@Enumerated(EnumType.STRING)
	private EAddressType addressType;
	
	@ManyToOne
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getIntNumber() {
		return intNumber;
	}

	public void setIntNumber(String intNumber) {
		this.intNumber = intNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public int getZipPostcode() {
		return zipPostcode;
	}

	public void setZipPostcode(int zipPostcode) {
		this.zipPostcode = zipPostcode;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public EAddressType getAddressType() {
		return addressType;
	}

	public void setAddressType(EAddressType addressType) {
		this.addressType = addressType;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this == o)
			return true;
		
		if(!(o instanceof Address))
			return false;
		
		Address other = (Address) o;
		return id != null &&
				Objects.equals(id, other.id);
	}
	
	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public String toString() {
		return "Address [number=" + number + ", intNumber=" + intNumber + ", street=" + street + ", zipPostcode="
				+ zipPostcode + ", city=" + city + ", addressType=" + addressType + "]";
	}

}
