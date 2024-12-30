package org.example.ecommerce.messaging;

import org.example.ecommerce.service.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ReviewMessageConsumer {

    private ProductService productService;
    private RestTemplate restTemplate;

    @Value(value = "${review-service.url}")
    private String reviewServiceUrl;

    public ReviewMessageConsumer(ProductService productService, RestTemplate restTemplate) {
        this.productService = productService;
        this.restTemplate = restTemplate;
    }

    @RabbitListener(queues = "productRatingQueue")
    public void consumeReviewMessage(ReviewMessage reviewMessage) {
        System.out.println(reviewMessage);
        String baseUrl = reviewServiceUrl+"/averageRating";
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("productId", reviewMessage.getProductId());

        String urlWithParams = builder.toUriString();
        Double avgRating = restTemplate.getForObject(urlWithParams, Double.class);
        productService.updateProductAvgRating(reviewMessage.getProductId(),avgRating);
    }


}
