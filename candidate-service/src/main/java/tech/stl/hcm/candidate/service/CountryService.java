package tech.stl.hcm.candidate.service;

import tech.stl.hcm.common.dto.CountryDTO;
import java.util.List;

public interface CountryService {
    List<CountryDTO> getAllCountries();
    CountryDTO getCountryById(Integer countryId);
    CountryDTO getCountryByCode(String countryCode);
    List<CountryDTO> getActiveCountries();
} 