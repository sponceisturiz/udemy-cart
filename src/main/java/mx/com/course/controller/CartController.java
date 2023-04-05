package mx.com.course.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import mx.com.course.broker.enumerable.OperationType;
import mx.com.course.exception.custom.CartIsEmptyException;
import mx.com.course.exception.custom.CourseAlreadyInCartException;
import mx.com.course.exception.custom.CourseNotInUserCartException;
import mx.com.course.model.CoursePriceVO;
import mx.com.course.model.TotalPriceVO;
import mx.com.course.model.request.CartRequest;
import mx.com.course.service.CartsService;

@Tag(name="CartController", 
    description="Controller for Cart Management Service")
@RestController
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    CartsService cartsService;

    @Operation(summary="Retrieve user cart by user id")
    @GetMapping(value="/{user}", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<CoursePriceVO>> getUserCart(@PathVariable("user") long userId) throws CartIsEmptyException{
        return ResponseEntity.ok().body(cartsService.retrieveUserCart(userId));
    }

    @Operation(summary="Retrieve total amount of articles in user cart by user id")
    @GetMapping(value="/{user}/total", produces=MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TotalPriceVO> getTotalPriceFromUserCart(@PathVariable("user") long userId) throws CartIsEmptyException{
        return ResponseEntity.ok().body(cartsService.calculateTotalUserCart(userId));
    }

    @Operation(summary="updates by adding or removing articles in user cart by user id")
    @PutMapping("/{user}")
    public ResponseEntity<Void> updateCart(
        @Parameter(description="user who is doing this operation") @PathVariable("user") long userId, 
        @Parameter(description="operation that you want to make('0' is for add, '1' is for remove)") @RequestParam("oper") byte type,
        @RequestBody CartRequest cartRequest) throws CourseNotInUserCartException, CourseAlreadyInCartException{
            cartsService.updateCartByUser(
                type==0? OperationType.Request.ADD : type==1 ? OperationType.Request.REMOVE: OperationType.Request.ADD, 
                userId, 
                cartRequest);
            return ResponseEntity.accepted().build();
    }
}
