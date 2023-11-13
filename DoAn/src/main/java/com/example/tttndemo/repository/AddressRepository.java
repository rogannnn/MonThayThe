package com.example.tttndemo.repository;

import com.example.tttndemo.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    @Query("SELECT a FROM Address a WHERE a.user.id = ?1 ORDER BY a.isDefault DESC")
    List<Address> getListAddressByUser(Integer userId);

    Long countById(Integer id);


    boolean existsAddressById(Integer addressId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = true WHERE a.id = ?1")
    void setDefaultAddress(Integer addressId);

    @Modifying
    @Query("UPDATE Address a SET a.isDefault = false WHERE a.user.id = ?1 AND a.id <> ?2")
    void setNonDefaultAddress(Integer userId, Integer addressId);

    @Query("SELECT a FROM Address a WHERE a.user.id = ?1 AND a.isDefault = true")
    Address findDefaultAddress(Integer id);

    @Query("SELECT a FROM Address a WHERE a.user.id = ?1 AND a.isDefault = true")
    Address getAddressByDefault(Integer userId);
}
