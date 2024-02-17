package uz.fayz.repository;

import uz.fayz.model.Address;
import uz.fayz.model.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class RegionRepository extends BaseDatabase {

    public List<Region> getRegionList() {
        List<Region> regions = new ArrayList<>();
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from region ");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Region region = Region.builder()
                        .name(rs.getString("name"))
                        .id(rs.getInt("id"))
                        .build();
                regions.add(region);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return regions;
    }


   public List<Address> getAddresses(int addressId) {
        List<Address> addresses = new ArrayList<>();
        try (Connection connection = connection()) {
            PreparedStatement ps = connection.prepareStatement("select * from address where region_id = ?");
            ps.setInt(1, addressId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Address address = Address.builder()
                        .city(rs.getString("city"))
                        .id(rs.getInt("id"))
                        .build();
                addresses.add(address);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addresses;
    }
}
