package library.code.service;

import library.code.dto.publisherDTO.PublisherCreateDTO;
import library.code.dto.publisherDTO.PublisherDTO;
import library.code.dto.publisherDTO.PublisherUpdateDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.PublisherMapper;
import library.code.repositories.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublisherService {
    private final PublisherRepository publisherRepository;
    private final PublisherMapper publisherMapper;

    public List<PublisherDTO> getAllPublishers() {
        return publisherRepository.findAll().stream()
                .map(publisherMapper::map)
                .collect(Collectors.toList());
    }

    public PublisherDTO getPublisher(Long id) {
        var publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with id: " + id + " not found"));
        return publisherMapper.map(publisher);
    }

    public PublisherDTO createPublisher(PublisherCreateDTO createDTO) {
        var publisher = publisherMapper.map(createDTO);
        publisherRepository.save(publisher);
        return publisherMapper.map(publisher);
    }

    public PublisherDTO updatePublisher(PublisherUpdateDTO updateDTO, Long id) {
        var publisher = publisherRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Publisher with id: " + id + " not found"));
        publisherMapper.update(updateDTO, publisher);
        publisherRepository.save(publisher);
        return publisherMapper.map(publisher);
    }

    public void deletePublisher(Long id) {
        publisherRepository.deleteById(id);
    }
}

