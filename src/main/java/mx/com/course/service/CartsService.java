package mx.com.course.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mx.com.course.actions.CourseAction;
import mx.com.course.broker.enumerable.OperationType;
import mx.com.course.broker.message.course.CoursePriceReplyMessage;
import mx.com.course.exception.custom.CartIsEmptyException;
import mx.com.course.exception.custom.CourseAlreadyInCartException;
import mx.com.course.exception.custom.CourseNotInUserCartException;
import mx.com.course.model.Cart;
import mx.com.course.model.CoursePriceVO;
import mx.com.course.model.TotalPriceVO;
import mx.com.course.model.request.CartRequest;
import mx.com.course.repository.CartsRepository;

@Service
public class CartsService {
    
    @Autowired
    CartsRepository cartsRepository;

    @Autowired
    CourseAction courseAction;

    public List<CoursePriceVO> retrieveUserCart(long userId) throws CartIsEmptyException{
        List<Cart> coursesToBuy = cartsRepository.findByUserId(userId);
        CoursePriceReplyMessage message = null;
        if(coursesToBuy.isEmpty())
            throw new CartIsEmptyException();
  
        try{
            message = courseAction.publishAndReply(userId, coursesToBuy.stream().map(course -> course.getCourseId()).toList());

            if(message.getCourseIdList().isEmpty())
                throw new CartIsEmptyException("courses not exist but present in cart");

            return message.getCourseIdList();
        }catch(InterruptedException | ExecutionException e){
            throw new CartIsEmptyException("comm issues with Course");
        }
    }

    public TotalPriceVO calculateTotalUserCart(long userId) throws CartIsEmptyException{

        List<CoursePriceVO> listCoursesInCart = retrieveUserCart(userId);

        BigDecimal total = listCoursesInCart.stream().map(course -> new BigDecimal(course.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);

        return new TotalPriceVO(total.setScale(2));
    }

    public void updateCartByUser(OperationType.Request type, long userId, CartRequest cartRequest) throws CourseNotInUserCartException, CourseAlreadyInCartException{

        if(type == OperationType.Request.ADD){
            addCourseFromUserCart(userId, cartRequest.getCourseId());
        }else if(type == OperationType.Request.REMOVE){
            removeCourseFromUserCart(userId, cartRequest.getCourseId());
        }
    }

    public void addCourseFromUserCart(long userId, long courseId) throws CourseAlreadyInCartException{
        if(findCourseByUserIdAndCourseId(userId, courseId))
            throw new CourseAlreadyInCartException();

        cartsRepository.save(new Cart(0, userId, courseId));
    }

    public void removeCourseFromUserCart(long userId, long courseId) throws CourseNotInUserCartException{
        
        if(!findCourseByUserIdAndCourseId(userId, courseId))
            throw new CourseNotInUserCartException();
        
        Optional<Cart> courseInUserCart = cartsRepository.findByUserIdAndCourseId(userId, courseId);
        cartsRepository.delete(courseInUserCart.get());
    }

    public void removeCourseFromAllCarts(long courseId){
        List<Cart> courseInCarts = cartsRepository.findByCourseId(courseId);

        cartsRepository.deleteAll(courseInCarts);
    }

    public void removeUserCart(long userId){
        List<Cart> userCart = cartsRepository.findByUserId(userId);

        cartsRepository.deleteAll(userCart);
    }

    public List<Long> findUserCartForMessaging(long userId) throws CartIsEmptyException{
        List<Cart> userCart = cartsRepository.findByUserId(userId);

        if(userCart.isEmpty())
            throw new CartIsEmptyException();

        return userCart.stream().map(element -> element.getCourseId()).collect(Collectors.toList());
    }

    private boolean findCourseByUserIdAndCourseId(long userId, long courseId){
        Optional<Cart> courseInUserCart = cartsRepository.findByUserIdAndCourseId(userId, courseId);

        return courseInUserCart.isPresent();
    }
}
