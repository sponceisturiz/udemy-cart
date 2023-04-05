package mx.com.course.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="carts")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Cart {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_generator")
    @SequenceGenerator(name="cart_generator", sequenceName = "cart_seq", allocationSize = 1)
    private long cartId;
    
    @Column(name="user_id")
    private long userId;
    
    @Column(name="course_id")
    private long courseId;
}
