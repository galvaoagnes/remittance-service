package com.inter.remittance.security;

import com.inter.remittance.domain.entities.Person;
import com.inter.remittance.domain.repositories.PersonRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class LoadUserDetails implements UserDetailsService {

    private final PersonRepository personRepository;


    public LoadUserDetails(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username){

        Person person = personRepository.findByDocumentValue(username);

        if (person == null){
            throw new RuntimeException("User not found");
        }

        return User.withUsername(person.document().value())
                .password(person.password())
                .roles(person.document().type().name())
                .build();
    }

}
