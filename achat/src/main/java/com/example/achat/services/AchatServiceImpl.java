package com.example.achat.services;

import com.example.achat.DTOs.AchatDTO;
import com.example.achat.DTOs.AchatReq;
import com.example.achat.DTOs.ProductDTO;
import com.example.achat.entities.Achat;
import com.example.achat.mappers.AchatMapper;
import com.example.achat.repositories.AchatRepo;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class AchatServiceImpl implements AchatService {

    @Autowired
    private AchatRepo achatRepository;

    @Autowired
    private AchatMapper achatMapper;

    private final WebClient webClient;

    @Autowired
    public AchatServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Override
    public AchatDTO createAchat(AchatReq achatReq) {

        Achat achat = achatMapper.reqToEntity(achatReq);
        achat.setDate(new Date());

        List<ProductDTO> productDTOList = convertProductsPrices(
                achatReq.getCurrency(),
                fetchProductDTOs(
                        achat.getProductsIds()
                )
        );

        Double total = calculateTotal(productDTOList);

        achat.setTotal(total);

        achatRepository.save(achat);

        return achatMapper.toDto(achat, productDTOList);
    }

    private Double calculateTotal(List<ProductDTO> productsList) {
        return productsList.stream()
                .map(ProductDTO::getPrice)
                .reduce(0.0, Double::sum);

    }

    private List<ProductDTO> convertProductsPrices ( String currency, List<ProductDTO> productDTOList){
        Double exchangeRate = fetchExchangeRate(currency);
        for (ProductDTO productDTO: productDTOList
             ) {
            productDTO.setPrice(
                    productDTO.getPrice() * exchangeRate
            );
        }
        return productDTOList;
    }

    private Double fetchExchangeRate(String currency) {
        try {
            log.info("Fetching exchange rate for currency: {}", currency);

            JsonNode response = webClient.get()
                    .uri("https://v6.exchangerate-api.com/v6/e686d5ca40f602c6f7a4f26c/latest/EUR")
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();

            //log.info("Full API response: {}", response);

            if (response == null || !response.has("conversion_rates")) {
                log.error("Null response or missing conversion rates in API response");
                throw new RuntimeException("Failed to fetch exchange rates. Response was null or incomplete.");
            }

            JsonNode conversionRates = response.get("conversion_rates");
            if (conversionRates == null || !conversionRates.has(currency)) {
                log.error("No exchange rate found for currency: {}", currency);
                throw new RuntimeException("Exchange rate for currency " + currency + " not found.");
            }

            Double exchangeRate = conversionRates.get(currency).asDouble();
            log.info("Fetched exchange rate for {}: {}", currency, exchangeRate);
            return exchangeRate;

        } catch (Exception e) {
            log.error("Failed to fetch exchange rates: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch exchange rates.", e);
        }
    }


    @Override
    public AchatDTO updateAchat(Long id, AchatDTO achatDTO) {
        Optional<Achat> achatOptional = achatRepository.findById(id);
        if (achatOptional.isPresent()) {
            Achat achat = achatOptional.get();
            achat.setDate(achatDTO.getDate());
            achat.setCurrency(achatDTO.getCurrency());
            achat.setTotal(achatDTO.getTotal());

            List<Long> productIds = achatDTO.getProducts().stream()
                    .map(ProductDTO::getId)
                    .collect(Collectors.toList());
            achat.setProductsIds(productIds);

            Achat updatedAchat = achatRepository.save(achat);

            List<ProductDTO> productDTOs = fetchProductDTOs(productIds);
            return achatMapper.toDto(updatedAchat, productDTOs);
        } else {
            throw new RuntimeException("Achat not found with id " + id);
        }
    }

    private List<ProductDTO> fetchProductDTOs(List<Long> productIds) {
        List<ProductDTO> productDTOList = new ArrayList<>();
        ProductDTO productDTO = new ProductDTO();
        for (Long productId: productIds
             ) {
            productDTO = webClient.get()
                    .uri("http://localhost:8080/api/products/"+productId)
                    .retrieve()
                    .bodyToMono(ProductDTO.class)
                    .block();
            productDTOList.add(productDTO);
        }
        return productDTOList;
    }

    @Override
    public AchatDTO getAchatById(Long id) {
        Achat achat = achatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achat not found with id " + id));
        List<ProductDTO> productDTOList = fetchProductDTOs(achat.getProductsIds());
        return achatMapper.toDto(achat, productDTOList);
    }

    @Override
    public List<AchatDTO> getAllAchats() {
        List<Achat> achats = achatRepository.findAll();
        List<AchatDTO> achatDTOList = new ArrayList<>();
        List<ProductDTO> productDTOList = new ArrayList<>();
        AchatDTO achatDTO = new AchatDTO();
        for (Achat achat: achats
             ) {
            productDTOList = fetchProductDTOs(achat.getProductsIds());
            achatDTO = achatMapper.toDto(achat, productDTOList);
            achatDTOList.add(achatDTO);
        }
        return achatDTOList;
    }

    @Override
    public void deleteAchat(Long id) {
        Achat achat = achatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Achat not found with id " + id));
        achatRepository.delete(achat);
    }
}
