package tech.stl.hcm.organization.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import tech.stl.hcm.common.db.entities.Organization;
import tech.stl.hcm.common.db.repositories.OrganizationRepository;
import tech.stl.hcm.common.dto.OrganizationDTO;
import tech.stl.hcm.common.dto.helpers.OrganizationCreateDTO;
import tech.stl.hcm.common.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<OrganizationDTO> findAll() {
        return organizationRepository.findAll().stream()
                .map(org -> modelMapper.map(org, OrganizationDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<OrganizationDTO> findAllPaginated(Pageable pageable) {
        return organizationRepository.findAll(pageable)
                .map(org -> modelMapper.map(org, OrganizationDTO.class));
    }

    @Override
    public Optional<OrganizationDTO> findById(UUID organizationId) {
        return organizationRepository.findById(organizationId)
                .map(org -> modelMapper.map(org, OrganizationDTO.class));
    }

    @Override
    public OrganizationDTO createOrganization(OrganizationDTO organizationDTO) {
        Optional<Organization> existingOrganization = organizationRepository.findByName(organizationDTO.getName());
        if (existingOrganization.isPresent()) {
            return modelMapper.map(existingOrganization.get(), OrganizationDTO.class);
        }
        Organization organization = modelMapper.map(organizationDTO, Organization.class);
        organization = organizationRepository.save(organization);
        return modelMapper.map(organization, OrganizationDTO.class);
    }

    @Override
    public OrganizationDTO createOrganization(OrganizationCreateDTO createDTO) {
        Organization organization = modelMapper.map(createDTO, Organization.class);
        organization = organizationRepository.save(organization);
        return modelMapper.map(organization, OrganizationDTO.class);
    }

    @Override
    public OrganizationDTO updateOrganization(UUID organizationId, OrganizationDTO organizationDTO) {
        Organization existingOrganization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found with id: " + organizationId));
        modelMapper.map(organizationDTO, existingOrganization);
        existingOrganization.setOrganizationId(organizationId); // Ensure the ID is not changed
        Organization updatedOrganization = organizationRepository.save(existingOrganization);
        return modelMapper.map(updatedOrganization, OrganizationDTO.class);
    }

    @Override
    public void deleteOrganization(UUID organizationId) {
        if (!organizationRepository.existsById(organizationId)) {
            throw new ResourceNotFoundException("Organization not found with id: " + organizationId);
        }
        organizationRepository.deleteById(organizationId);
    }
} 