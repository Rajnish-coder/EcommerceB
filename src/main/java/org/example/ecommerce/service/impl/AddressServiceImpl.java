package org.example.ecommerce.service.impl;

import org.example.ecommerce.exceptions.APIException;
import org.example.ecommerce.exceptions.ResourceNotFoundException;
import org.example.ecommerce.model.Address;
import org.example.ecommerce.model.User;
import org.example.ecommerce.payload.AddressDTO;
import org.example.ecommerce.repository.AddressRepository;
import org.example.ecommerce.repository.UserRepository;
import org.example.ecommerce.service.AddressService;
import org.example.ecommerce.util.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDTO createAddress(AddressDTO addressDTO, User user) {
        List<Address> addresses = user.getAddresses();
        Address address = modelMapper.map(addressDTO, Address.class);
        if(addresses == null) {
            addresses = new ArrayList<>();
        }
        addresses.add(address);
        user.setAddresses(addresses);
        address.setUser(user);
        Address savedAddress = addressRepository.save(address);
        return modelMapper.map(savedAddress, AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAddresses() {
        List<Address> addresses = addressRepository.findAll();
        List<AddressDTO> addressDTOS = addresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public AddressDTO getAddressById(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        return modelMapper.map(address,AddressDTO.class);
    }

    @Override
    public List<AddressDTO> getAllAddressesByUser(User currentUser) {
        List<Address> addresses = currentUser.getAddresses();
        if(addresses.isEmpty()) {
            throw new APIException("No addresses found");
        }
        List<AddressDTO> addressDTOS = addresses.stream()
                .map(address -> modelMapper.map(address,AddressDTO.class)).toList();
        return addressDTOS;
    }

    @Override
    public String deleteAddressById(Long addressId) {
        Address addressFromDb = addressRepository.findById(addressId)
                .orElseThrow(() -> new ResourceNotFoundException("Address", "id", addressId));
        User user = addressFromDb.getUser();
        user.getAddresses().removeIf(address -> address.getAddressId().equals(addressId));
        userRepository.save(user);
        addressRepository.delete(addressFromDb);
        return "Address deleted Successfully with address id " + addressId;
    }


}
