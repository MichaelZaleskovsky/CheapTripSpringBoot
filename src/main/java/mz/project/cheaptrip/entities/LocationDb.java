package mz.project.cheaptrip.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"id"})
public class LocationDb {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
}
