package com.example.ecommerce.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long userId;
	
	@NotBlank
	@Column(name="username")
	private String userName;
	
	@NotBlank
	private String email;
	
	@NotBlank
	private String password;
	
	@ManyToMany(cascade= {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER) // Cascade:persists/merge - Agar user ko save/update krenge then usse associated roles bhi autosave ho jyega.
	@JoinTable(name = "user_role",
			   joinColumns = @JoinColumn(name="user_id"), // user_id column will be created in the user_role that will be refering the primary key of user Entity
			   inverseJoinColumns = @JoinColumn(name="role_id") // role_id column will be created in the user_role that will be referring the primary key of role entity
				)
	private Set<Role> roles = new HashSet<>();
	
	
	
}