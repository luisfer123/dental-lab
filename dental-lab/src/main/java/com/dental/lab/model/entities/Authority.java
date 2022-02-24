package com.dental.lab.model.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.dental.lab.model.enums.EAuthority;

@Entity
@Table(name = "Authority")
public class Authority {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name = "authority")
	@Enumerated(EnumType.STRING)
	private EAuthority authority;
	
	@Column(name = "description")
	private String description;
	
	private Set<User> users;

}
