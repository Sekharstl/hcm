package tech.stl.hcm.candidate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tech.stl.hcm.common.db.repositories.CountryRepository;
import tech.stl.hcm.common.dto.CountryDTO;
import tech.stl.hcm.common.mapper.CountryMapper;
import tech.stl.hcm.common.db.entities.Country;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryRepository countryRepository;
    private final CountryMapper countryMapper;

    @Override
    public List<CountryDTO> getAllCountries() {
        return countryRepository.findAll().stream()
                .map(countryMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CountryDTO getCountryById(Integer countryId) {
        Country country = countryRepository.findById(countryId)
                .orElseThrow(() -> new RuntimeException("Country not found with id: " + countryId));
        return countryMapper.toDTO(country);
    }

    @Override
    public CountryDTO getCountryByCode(String countryCode) {
        Country country = countryRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new RuntimeException("Country not found with code: " + countryCode));
        return countryMapper.toDTO(country);
    }

    @Override
    public List<CountryDTO> getActiveCountries() {
        return countryRepository.findActiveCountries().stream()
                .map(countryMapper::toDTO)
                .collect(Collectors.toList());
    }
} 