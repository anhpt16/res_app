package com.anhpt.res_app.admin.service;

import com.anhpt.res_app.admin.dto.AdminDeskMapper;
import com.anhpt.res_app.admin.dto.request.desk.*;
import com.anhpt.res_app.admin.dto.response.desk.DeskDurationResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskMediaResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskResponse;
import com.anhpt.res_app.admin.dto.response.desk.DeskShortResponse;
import com.anhpt.res_app.admin.filter.AdminDeskFilter;
import com.anhpt.res_app.admin.validation.AdminDeskValidation;
import com.anhpt.res_app.common.dto.response.PageResponse;
import com.anhpt.res_app.common.entity.*;
import com.anhpt.res_app.common.entity.key.DeskMediaId;
import com.anhpt.res_app.common.enums.DeskPosition;
import com.anhpt.res_app.common.enums.DeskSeat;
import com.anhpt.res_app.common.enums.DeskType;
import com.anhpt.res_app.common.enums.status.DeskDurationStatus;
import com.anhpt.res_app.common.enums.status.DeskStatus;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminDeskService {
    // Repository
    private final DeskRepository deskRepository;
    private final MediaRepository mediaRepository;
    private final DeskMediaRepository deskMediaRepository;
    private final DurationRepository durationRepository;
    private final DeskDurationRepository deskDurationRepository;
    // Validation
    private final AdminDeskValidation adminDeskValidation;
    // Mapper
    private final AdminDeskMapper adminDeskMapper;
    // Filter
    private final AdminDeskFilter adminDeskFilter;

    // SECTION: Bàn
    // Tạo mới bàn
    public DeskResponse createDesk(DeskCreateRequest request) {
        adminDeskValidation.validateDeskCreate(request);
        DeskSeat deskSeat = DeskSeat.fromCode(request.getSeat());
        Desk desk = new Desk();
        desk.setNumber(request.getNumber());
        desk.setSeat(deskSeat);
        desk.setStatus(DeskStatus.INACTIVE);
        desk.setCreatedAt(LocalDateTime.now());
        desk.setUpdatedAt(LocalDateTime.now());
        if (request.getType() != null) {
            DeskType deskType = DeskType.fromCode(request.getType());
            desk.setType(deskType);
        }
        if (request.getPosition() != null) {
            DeskPosition deskPosition = DeskPosition.fromCode(request.getPosition());
            desk.setPosition(deskPosition);
        }
        if (request.getDescription() != null) desk.setDescription(request.getDescription());
        desk = deskRepository.save(desk);
        return adminDeskMapper.toDeskResponse(desk);
    }
    // Cập nhật thông tin bàn
    public DeskResponse updateDesk(Integer deskNumber, DeskUpdateRequest request) {
        adminDeskValidation.validateDeskUpdate(deskNumber, request);
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        if (request.getType() != null) {
            DeskType deskType = DeskType.fromCode(request.getType());
            desk.setType(deskType);
        }
        if (request.getPosition() != null) {
            DeskPosition deskPosition = DeskPosition.fromCode(request.getPosition());
            desk.setPosition(deskPosition);
        }
        if (request.getSeat() != null) {
            DeskSeat deskSeat = DeskSeat.fromCode(request.getSeat());
            desk.setSeat(deskSeat);
        }
        if (request.getDescription() != null) desk.setDescription(request.getDescription());
        desk.setUpdatedAt(LocalDateTime.now());
        desk = deskRepository.save(desk);
        return adminDeskMapper.toDeskResponse(desk);
    }
    // Lấy thông tin chi tiết của bàn
    public DeskResponse getDesk(Integer deskNumber) {
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        return adminDeskMapper.toDeskResponse(desk);
    }
    // Cập nhật trạng thái của bàn
    public DeskResponse updateDeskStatus(Integer deskNumber, String status) {
        adminDeskValidation.validateDeskStatusUpdate(deskNumber, status);
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        DeskStatus deskStatus = DeskStatus.fromCode(status);
        desk.setStatus(deskStatus);
        desk.setUpdatedAt(LocalDateTime.now());
        desk = deskRepository.save(desk);
        return adminDeskMapper.toDeskResponse(desk);
    }
    // Lấy danh sách bàn
    public PageResponse<DeskShortResponse> getDesks(DeskSearchRequest request) {
        adminDeskValidation.validateDeskSearch(request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Specification<Desk> specification = adminDeskFilter.search(request);
        Page<Desk> desks = deskRepository.findAll(specification, pageable);
        List<DeskShortResponse> deskShortResponses = desks.stream()
            .map(adminDeskMapper::toDeskShortResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            deskShortResponses,
            request.getPage(),
            request.getSize(),
            desks.getTotalElements(),
            desks.getTotalPages()
        );
    }

    // SECTION: Ảnh của bàn
    // Thêm ảnh cho một bàn (cần trả về DeskMedia ngay lập tức -> lưu trực tiếp DeskMedia)
    public DeskMediaResponse addDeskMedia(Integer deskNumber, Long mediaId, DeskMediaRequest request) {
        adminDeskValidation.validateDeskMediaAdd(deskNumber, mediaId, request);
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        Media media = mediaRepository.findById(mediaId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy media"));
        DeskMediaId deskMediaId = new DeskMediaId(deskNumber, mediaId);
        DeskMedia deskMedia = new DeskMedia();
        deskMedia.setId(deskMediaId);
        deskMedia.setDesk(desk);
        deskMedia.setMedia(media);
        if (request.getDisplayOrder() == null) {
            deskMedia.setDisplayOrder(0);
        } else {
            deskMedia.setDisplayOrder(request.getDisplayOrder());
        }
        deskMedia.setCreatedAt(LocalDateTime.now());
        deskMedia = deskMediaRepository.save(deskMedia);
        return adminDeskMapper.toDeskMediaResponse(deskMedia);
    }
    // Cập nhật ảnh cho bàn
    public DeskMediaResponse updateDeskMedia(Integer deskNumber, Long mediaId, DeskMediaRequest request) {
        adminDeskValidation.validateDeskMediaUpdate(deskNumber, mediaId, request);
        DeskMediaId deskMediaId = new DeskMediaId(deskNumber, mediaId);
        DeskMedia deskMedia = deskMediaRepository.findById(deskMediaId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DeskMedia"));
        if (request.getDisplayOrder() != null) {
            deskMedia.setDisplayOrder(request.getDisplayOrder());
        }
        deskMedia = deskMediaRepository.save(deskMedia);
        return adminDeskMapper.toDeskMediaResponse(deskMedia);
    }
    // Xóa ảnh khỏi bàn
    public void deleteDeskMedia(Integer deskNumber, Long mediaId) {
        adminDeskValidation.validateDeskMediaDelete(deskNumber, mediaId);
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        desk.getDeskMedias().removeIf(dm -> dm.getId().getMediaId().equals(mediaId));
        deskRepository.save(desk);
    }
    // Lấy danh sách ảnh của bàn
    public List<DeskMediaResponse> getDeskMedias(Integer deskNumber) {
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        return desk.getDeskMedias().stream()
            .map(adminDeskMapper::toDeskMediaResponse)
            .collect(Collectors.toList());
    }

    // SECTION: Thời lượng bàn
    // Thêm thời lượng cho bàn (cần trả về DeskDuration ngay lập tức -> lưu trực tiếp DeskDuration)
    public DeskDurationResponse addDeskDuration(Integer deskNumber, Long durationId, DeskDurationRequest request) {
        adminDeskValidation.validateDeskDurationAdd(deskNumber, durationId, request);
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        Duration duration = durationRepository.findById(durationId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy duration"));
        DeskDuration deskDuration = new DeskDuration();
        deskDuration.setDesk(desk);
        deskDuration.setDuration(duration);
        deskDuration.setPrice(request.getPrice());
        deskDuration.setStatus(DeskDurationStatus.INACTIVE);
        deskDuration.setCreatedAt(LocalDateTime.now());
        deskDuration.setUpdatedAt(LocalDateTime.now());
        deskDuration = deskDurationRepository.save(deskDuration);
        return adminDeskMapper.toDeskDurationResponse(deskDuration);
    }
    // Cập nhật thông tin thời lượng của bàn
    public DeskDurationResponse updateDeskDuration(Integer deskNumber, Long durationId, DeskDurationRequest request) {
        adminDeskValidation.validateDeskDurationUpdate(deskNumber, durationId, request);
        DeskDuration deskDuration = deskDurationRepository.findByDeskNumberAndDurationId(deskNumber, durationId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DeskDuration"));
        if (request.getPrice() != null) {
            deskDuration.setPrice(request.getPrice());
        }
        deskDuration.setUpdatedAt(LocalDateTime.now());
        deskDuration = deskDurationRepository.save(deskDuration);
        return adminDeskMapper.toDeskDurationResponse(deskDuration);
    }
    // Cập nhật trạng thái thời lượng của bàn
    public DeskDurationResponse updateDeskDurationStatus(Integer deskNumber, Long durationId, String status) {
        adminDeskValidation.validateDeskDurationStatusUpdate(deskNumber, durationId, status);
        DeskDuration deskDuration = deskDurationRepository.findByDeskNumberAndDurationId(deskNumber, durationId)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy DeskDuration"));
        DeskDurationStatus deskDurationStatus = DeskDurationStatus.fromCode(status);
        deskDuration.setStatus(deskDurationStatus);
        deskDuration.setUpdatedAt(LocalDateTime.now());
        deskDuration = deskDurationRepository.save(deskDuration);
        return adminDeskMapper.toDeskDurationResponse(deskDuration);
    }
    // Xóa thời lượng của một bàn
    public void deleteDeskDuration(Integer deskNumber, Long durationId) {
        adminDeskValidation.validateDeskDurationDelete(deskNumber, durationId);
        Desk desk = deskRepository.findById(deskNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bàn"));
        desk.getDeskDurations().removeIf(dd -> dd.getDuration().getId().equals(durationId));
        deskRepository.save(desk);
    }
    // Lấy danh sách thời lượng của bàn
    public PageResponse<DeskDurationResponse> getDeskDurations(Integer deskNumber, DeskDurationSearchRequest request) {
        adminDeskValidation.validateDeskDurationSearch(deskNumber, request);
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());
        Specification<DeskDuration> specification = adminDeskFilter.deskDurationSearch(deskNumber, request);
        Page<DeskDuration> deskDurations = deskDurationRepository.findAll(specification, pageable);
        List<DeskDurationResponse> deskDurationResponses = deskDurations.stream()
            .map(adminDeskMapper::toDeskDurationResponse)
            .collect(Collectors.toList());
        return new PageResponse<>(
            deskDurationResponses,
            request.getPage(),
            request.getSize(),
            deskDurations.getTotalElements(),
            deskDurations.getTotalPages()
        );
    }

}
