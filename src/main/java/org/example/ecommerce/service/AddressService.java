package org.example.ecommerce.service;

import org.example.ecommerce.model.User;
import org.example.ecommerce.payload.AddressDTO;

import java.util.List;

public interface AddressService {
    AddressDTO createAddress(AddressDTO addressDTO, User user);

    List<AddressDTO> getAddresses();

    AddressDTO getAddressById(Long addressId);

    List<AddressDTO> getAllAddressesByUser(User currentUser);

    String deleteAddressById(Long addressId);
}
