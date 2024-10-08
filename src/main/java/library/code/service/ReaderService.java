package library.code.service;

import library.code.dto.ReaderDTO.ReaderCreateDTO;
import library.code.dto.ReaderDTO.ReaderDTO;
import library.code.dto.ReaderDTO.ReaderUpdateDTO;
import library.code.dto.specificationDTO.ReaderParamDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.ReaderMapper;
import library.code.repositories.ReaderRepository;
import library.code.specification.ReaderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReaderService {

    private final ReaderRepository readerRepository;
    private final LibraryCardService libraryCardService;
    private final ReaderMapper readerMapper;
    private final ReaderSpecification specification;

    public List<ReaderDTO> getAllReaders(ReaderParamDTO params, @RequestParam(defaultValue = "1") int page) {
        var spec = specification.build(params);
        return readerRepository.findAll(spec, PageRequest.of(page - 1, 10)).stream()
                .map(readerMapper::map)
                .collect(Collectors.toList());
    }

    public ReaderDTO getReader(Long id) {
        var reader = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader with id: " + id + " not found"));
        return readerMapper.map(reader);
    }

    public ReaderDTO createReader(ReaderCreateDTO createDTO) {
        var reader = readerMapper.map(createDTO);
        reader.setLibraryCard(libraryCardService.createLibraryNumberCard(reader));
        readerRepository.save(reader);
        return readerMapper.map(reader);
    }

    public ReaderDTO updateReader(ReaderUpdateDTO updateDTO, Long id) {
        var reader = readerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reader with id: " + id + " not found"));
        readerMapper.update(updateDTO, reader);
        readerRepository.save(reader);
        return readerMapper.map(reader);
    }

    public void deleteReader(Long id) {
        readerRepository.deleteById(id);
    }
}
