package uz.fayz.service;

import uz.fayz.model.Address;
import uz.fayz.model.Region;
import uz.fayz.repository.RegionRepository;

import java.util.List;

public class RegionService {
    private static final RegionRepository regionRepository = new RegionRepository();

    public List<Region> getRegions() {
        return regionRepository.getRegionList();
    }

    public List<Address> getAddress(int addressId) {
        return regionRepository.getAddresses(addressId);
    }
}
