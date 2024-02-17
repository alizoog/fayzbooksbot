package uz.fayz.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private Integer id;
    private Integer region_id;
    private String city;
}
