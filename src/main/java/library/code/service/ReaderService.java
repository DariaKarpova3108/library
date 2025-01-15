package library.code.service;

import library.code.dto.libraryCardDTO.LibraryCardDTO;
import library.code.dto.readerDTO.ReaderCreateDTO;
import library.code.dto.readerDTO.ReaderDTO;
import library.code.dto.readerDTO.ReaderUpdateDTO;
import library.code.dto.specificationDTO.ReaderParamDTO;
import library.code.exception.ResourceNotFoundException;
import library.code.mapper.LibraryCardMapper;
import library.code.mapper.ReaderMapper;
import library.code.models.Reader;
import library.code.models.RoleName;
import library.code.repositories.LibraryCardRepository;
import library.code.repositories.ReaderRepository;
import library.code.repositories.UserRepository;
import library.code.specification.ReaderSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReaderService {
    private final ReaderRepository readerRepository;
    private final LibraryCardService libraryCardService;
    private final LibraryCardMapper libraryCardMapper;
    private final LibraryCardRepository libraryCardRepository;
    private final UserRepository userRepository;
    private final ReaderMapper readerMapper;
    private final ReaderSpecification specification;

    public List<ReaderDTO> getAllReaders(ReaderParamDTO params, int page, String sort) {
        log.info("Fetching readers with params: {} | Page: {} | Sort: {}", params, page, sort);

        String[] sortParams = sort.split(", ");
        if (sortParams.length != 2 || !(sortParams[1].equals("asc") || sortParams[1].equals("desc"))) {
            log.error("Invalid sort direction: {}", sortParams[1]);
            throw new IllegalArgumentException("Invalid sort direction");
        }

        Sort sortOrder = Sort.by(sortParams[1].equals("asc") ? Sort.Order.asc(sortParams[0].trim())
                : Sort.Order.desc(sortParams[0].trim()));

        Specification<Reader> spec = specification.build(params, sortOrder);
        Pageable pageable = PageRequest.of(page - 1, 10);

        var readers = readerRepository.findAll(spec, pageable).stream()
                .map(readerMapper::map)
                .collect(Collectors.toList());

        log.info("Successfully fetched {} readers", readers.size());
        return readers;
    }

    public ReaderDTO getReader(Long id) {
        log.info("Fetching reader with ID: {}", id);

        var reader = readerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reader with ID: {} not found", id);
                    return new ResourceNotFoundException("Reader with ID: " + id + " not found");
                });

        log.info("Successfully fetched reader with ID: {}", id);
        return readerMapper.map(reader);
    }

    public LibraryCardDTO getLibraryCardByReaderId(Long readerId) {
        log.info("Fetching library card for reader with ID: {}", readerId);

        var reader = readerRepository.findById(readerId)
                .orElseThrow(() -> {
                    log.error("Reader with ID: {} not found", readerId);
                    return new ResourceNotFoundException("Reader with ID: " + readerId + " not found");
                });

        var libraryCard = libraryCardRepository.findByCardNumber(reader.getLibraryCard().getCardNumber())
                .orElseThrow(() -> {
                    log.error("Library card not found for reader with ID: {}", readerId);
                    return new ResourceNotFoundException("Library card not found");
                });

        log.info("Successfully fetched library card for reader with ID: {}", readerId);
        return libraryCardMapper.map(libraryCard);
    }

    public ReaderDTO completeReaderProfile(Long userId, ReaderCreateDTO createDTO) {
        log.info("Completing reader profile for user ID: {}", userId);

        var user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.error("User with ID: {} not found", userId);
                    return new ResourceNotFoundException("User with ID: " + userId + "not found");
                });

        if (user.getRole().getRoleName() != RoleName.READER) {
            log.error("User with ID: {} is not a reader", userId);
            throw new IllegalArgumentException("User is not a reader");
        }

        if (user.getReader() != null) {
            log.error("Reader profile already exists for user ID: {}", userId);
            throw new IllegalArgumentException("Reader profile already exists for this user");
        }

        var reader = readerMapper.map(createDTO);
        reader.setLibraryCard(libraryCardService.createLibraryNumberCard(reader));
        reader.setUser(user);
        readerRepository.save(reader);

        log.info("Successfully completed reader profile for user ID: {}", userId);
        return readerMapper.map(reader);
    }

    public ReaderDTO updateReader(ReaderUpdateDTO updateDTO, Long id) {
        log.info("Updating reader profile with ID: {}", id);

        var reader = readerRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Reader with ID: {} not found for update", id);
                    return new ResourceNotFoundException("Reader with ID: " + id + " not found");
                });

        readerMapper.update(updateDTO, reader);
        readerRepository.save(reader);
        log.info("Successfully updated reader profile with ID: {}", id);
        return readerMapper.map(reader);
    }

    public void deleteReader(Long id) {
        log.info("Attempting to delete reader with ID: {}", id);
        readerRepository.deleteById(id);
        log.info("Successfully deleted reader with ID: {}", id);
    }
}
