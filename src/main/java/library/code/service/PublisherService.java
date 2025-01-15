package library.code.service;

import library.code.dto.publisherDTO.PublisherCreateDTO;
import library.code.dto.publisherDTO.PublisherDTO;
import library.code.dto.publisherDTO.PublisherUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.PublisherMapper;
import library.code.repositories.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public List<PublisherDTO> getAllPublishers() {
        log.info("Fetching all publishers");

        var publishers = publisherRepository.findAll().stream()
                .map(publisherMapper::map)
                .collect(Collectors.toList());

        log.info("Successfully fetched {} publishers", publishers.size());
        return publishers;
    }

    public PublisherDTO getPublisher(Long id) {
        log.info("Fetching publisher with ID: {}", id);

        var publisher = publisherRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Publisher with ID: {} not found", id);
                    return new ResourceNotFoundException("Publisher with ID: " + id + " not found");
                });

        log.info("Successfully fetched publisher with ID: {}", id);
        return publisherMapper.map(publisher);
    }

    public PublisherDTO createPublisher(PublisherCreateDTO createDTO) {
        log.info("Creating a new publisher with name: {}", createDTO.getTitle());

        var publisher = publisherMapper.map(createDTO);
        publisherRepository.save(publisher);

        log.info("Successfully created publisher with name: {}", publisher.getTitle());
        return publisherMapper.map(publisher);
    }

    public PublisherDTO updatePublisher(PublisherUpdateDTO updateDTO, Long id) {
        log.info("Updating publisher with ID: {}", id);

        var publisher = publisherRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Publisher with ID: {} not found for update", id);
                    return new ResourceNotFoundException("Publisher with ID: " + id + " not found");
                });

        publisherMapper.update(updateDTO, publisher);
        publisherRepository.save(publisher);
        log.info("Successfully updated publisher with ID: {}", id);
        return publisherMapper.map(publisher);
    }

    public void deletePublisher(Long id) {
        log.info("Attempting to delete publisher with ID: {}", id);
        publisherRepository.deleteById(id);
        log.info("Successfully deleted publisher with ID: {}", id);
    }
}

