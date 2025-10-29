package com.Ecom.Ecom.Repo;

import com.Ecom.Ecom.Entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepo extends JpaRepository<Email,String> {
}
