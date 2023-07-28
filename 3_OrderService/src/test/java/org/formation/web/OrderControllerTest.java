package org.formation.web;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.formation.domain.Address;
import org.formation.domain.DeliveryInformation;
import org.formation.domain.Order;
import org.formation.domain.OrderItem;
import org.formation.domain.PaymentInformation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	public void postOrderShouldReturnCreatedAndAnId() throws Exception {
		Address address = Address.builder().codePostal("75019").rue("Rébeval").ville("Paris").build();
		DeliveryInformation deliveryInformation = DeliveryInformation.builder().deliveryTime(LocalDateTime.now()).address(address).build();
		PaymentInformation paymentInformation = PaymentInformation.builder().paymentToken("ATOKEN").build();
		OrderItem orderItem = OrderItem.builder().refProduct("REF").price(10.0f).quantity(2).build();
		List<OrderItem> orderItems = new ArrayList<>();
		orderItems.add(orderItem);
		Order order = Order.builder().deliveryInformation(deliveryInformation).paymentInformation(paymentInformation).orderItems(orderItems).build();
		String json = objectMapper.writeValueAsString(order);
		
		this.mockMvc.perform(post("/api/orders").content(json).contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").exists())
				.andExpect(content().string(containsString("ATOKEN")))
				.andExpect(content().string(containsString("75019")));
	}
}
