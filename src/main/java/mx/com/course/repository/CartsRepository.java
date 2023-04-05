package mx.com.course.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import mx.com.course.model.Cart;

@Repository
public interface CartsRepository extends CrudRepository<Cart, Long>{
    
    Optional<Cart> findByUserIdAndCourseId(long userId, long courseId);

    List<Cart> findByUserId(long userId);

    List<Cart> findByCourseId(long courseId);
}
