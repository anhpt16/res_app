package com.anhpt.res_app.admin.validation;

import com.anhpt.res_app.admin.dto.request.desk.*;
import com.anhpt.res_app.common.entity.*;
import com.anhpt.res_app.common.entity.key.DeskMediaId;
import com.anhpt.res_app.common.enums.DeskPosition;
import com.anhpt.res_app.common.enums.DeskSeat;
import com.anhpt.res_app.common.enums.DeskType;
import com.anhpt.res_app.common.enums.status.DeskDurationStatus;
import com.anhpt.res_app.common.enums.status.DeskStatus;
import com.anhpt.res_app.common.exception.MultiDuplicateException;
import com.anhpt.res_app.common.exception.ResourceNotFoundException;
import com.anhpt.res_app.common.repository.*;
import com.anhpt.res_app.common.utils.function.FieldNameUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class AdminDeskValidation {
    private final DeskRepository deskRepository;
    private final DeskMediaRepository deskMediaRepository;
    private final MediaRepository mediaRepository;
    private final DurationRepository durationRepository;
    private final DeskDurationRepository deskDurationRepository;
    // SECTION: Bàn
    public void validateDeskCreate(DeskCreateRequest request) {
        Map<String, String> errors = new HashMap<>();
        Optional<Desk> desk = deskRepository.findById(request.getNumber());
        // Kiểm tra deskNumber tồn tại
        if (desk.isPresent()) {
            String field = FieldNameUtil.getFieldName(DeskCreateRequest::getNumber);
            errors.put(field, "Đã tồn tại");
        }
        // Kiểm tra seat hợp lệ
        DeskSeat seat = DeskSeat.fromCode(request.getSeat());
        // Nếu có type, kiểm tra type hợp lệ
        if (request.getType() != null) {
            DeskType type = DeskType.fromCode(request.getType());
        }
        // Nếu có position, kiểm tra position hợp lệ
        if (request.getPosition() != null) {
            DeskPosition position = DeskPosition.fromCode(request.getPosition());
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDeskUpdate(Integer deskNumber, DeskUpdateRequest request) {
        // Kiểm tra request không rỗng
        if (request.isEmpty()) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
        Map<String, String> errors = new HashMap<>();
        Optional<Desk> desk = deskRepository.findById(deskNumber);
        if (!desk.isPresent()) {
            log.warn("Không tìm thấy bàn số: {}", deskNumber);
            throw new ResourceNotFoundException("Không tìm thấy bàn");
        }
        // Kiểm tra seat hợp lệ nếu có
        if (request.getSeat() != null) {
            DeskSeat seat = DeskSeat.fromCode(request.getSeat());
        }
        // Nếu có type, kiểm tra type hợp lệ
        if (request.getType() != null) {
            DeskType type = DeskType.fromCode(request.getType());
        }
        // Nếu có position, kiểm tra position hợp lệ
        if (request.getPosition() != null) {
            DeskPosition position = DeskPosition.fromCode(request.getPosition());
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDeskStatusUpdate(Integer deskNumber, String status) {
        // Kiểm tra desk tồn tại
        Optional<Desk> desk = deskRepository.findById(deskNumber);
        if (desk.isEmpty()) {
            log.warn("Không tìm thấy bàn số: {}", deskNumber);
            throw new ResourceNotFoundException("Không tìm thấy bàn");
        }
        // Kiểm tra status hợp lệ
        DeskStatus deskStatus = DeskStatus.fromCode(status);
        // Kiểm tra trùng lặp trạng thái
        if (deskStatus.equals(desk.get().getStatus())) {
            throw new IllegalArgumentException("Trạng thái đã tồn tại");
        }
    }

    public void validateDeskSearch(DeskSearchRequest request) {
        // Nếu có trạng thái, kiểm tra trạng thái hợp lệ
        if (request.getStatus() != null) {
            DeskStatus deskStatus = DeskStatus.fromCode(request.getStatus());
        }
        // Nếu có số chỗ ngồi, kiểm tra số chỗ ngồi hợp lệ
        if (request.getSeat() != null) {
            DeskSeat seat = DeskSeat.fromCode(request.getSeat());
        }
        // Nếu có kiểu bàn, kiểm tra kiểu bàn hợp lệ
        if (request.getType() != null) {
            DeskType type = DeskType.fromCode(request.getType());
        }
        // Nếu có vị trí bàn, kiểm tra vị trí bàn hợp lệ
        if (request.getPosition() != null) {
            DeskPosition position = DeskPosition.fromCode(request.getPosition());
        }
    }

    // SECTION: Ảnh của bàn
    public void validateDeskMediaAdd(Integer deskNumber, Long mediaId, DeskMediaRequest request) {
        // Kiểm tra desk tồn tại
        Optional<Desk> desk = deskRepository.findById(deskNumber);
        if (desk.isEmpty()) {
            log.warn("Không tìm thấy bàn số: {}", deskNumber);
            throw new ResourceNotFoundException("Không tìm thấy bàn");
        }
        // Kiểm tra media tồn tại
        Optional<Media> media = mediaRepository.findById(mediaId);
        if (media.isEmpty()) {
            log.warn("Không tìm thấy media: {}", mediaId);
            throw new ResourceNotFoundException("Không tìm thấy media");
        }
        // Kiểm tra người dùng sở hữu media
        // Long userId = SecurityContextUtils.getCurrentUserId();
        // if (userId == null) {
        //     throw new ResourceNotFoundException("Không tìm thấy người dùng");
        // }
        // if (!media.get().getUserId().equals(userId)) {
        //     throw new ResourceNotFoundException("Người dùng không sở hữu media");
        // }
        // Kiểm tra desk-media tồn tại
        DeskMediaId deskMediaId = new DeskMediaId(deskNumber, mediaId);
        Optional<DeskMedia> deskMedia = deskMediaRepository.findById(deskMediaId);
        if (deskMedia.isPresent()) {
            log.warn("Bàn {} đã có media: {}", deskNumber, mediaId);
            throw new IllegalArgumentException("Bàn đã có media");
        }
    }

    public void validateDeskMediaUpdate(Integer deskNumber, Long mediaId, DeskMediaRequest request) {
        // Kiểm tra desk-media tồn tại
        DeskMediaId deskMediaId = new DeskMediaId(deskNumber, mediaId);
        Optional<DeskMedia> deskMedia = deskMediaRepository.findById(deskMediaId);
        if (deskMedia.isEmpty()) {
            log.warn("Không tìm thấy desk-media: {}", deskMediaId);
            throw new ResourceNotFoundException("Không tìm thấy desk-media");
        }
        if (request.getDisplayOrder() == null) {
            throw new IllegalArgumentException("Dữ liệu không hợp lệ");
        }
    }

    public void validateDeskMediaDelete(Integer deskNumber, Long mediaId) {
        // Kiểm tra desk-media tồn tại
        DeskMediaId deskMediaId = new DeskMediaId(deskNumber, mediaId);
        Optional<DeskMedia> deskMedia = deskMediaRepository.findById(deskMediaId);
        if (deskMedia.isEmpty()) {
            log.warn("Không tìm thấy desk-media: {}", deskMediaId);
            throw new ResourceNotFoundException("Không tìm thấy desk-media");
        }
    }

    // SECTION: Thời lượng của bàn
    public void validateDeskDurationAdd(Integer deskNumber, Long durationId, DeskDurationRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra desk tồn tại
        Optional<Desk> desk = deskRepository.findById(deskNumber);
        if (desk.isEmpty()) {
            log.warn("Không tìm thấy bàn số: {}", deskNumber);
            throw new ResourceNotFoundException("Không tìm thấy bàn");
        }
        // Kiểm tra duration tồn tại
        Optional<Duration> duration = durationRepository.findById(durationId);
        if (duration.isEmpty()) {
            log.warn("Không tìm thấy duration: {}", durationId);
            throw new ResourceNotFoundException("Không tìm thấy duration");
        }
        // kiểm tra desk-duration tồn tại
        Optional<DeskDuration> deskDuration = deskDurationRepository.findByDeskNumberAndDurationId(deskNumber, durationId);
        if (deskDuration.isPresent()) {
            log.warn("Bàn {} đã có thời lượng: {}", deskNumber, durationId);
            throw new IllegalArgumentException("Bàn đã có thời lượng");
        }
        // Giá không được để trống
        if (request.getPrice() == null) {
            String field = FieldNameUtil.getFieldName(DeskDurationRequest::getPrice);
            errors.put(field, "Không được để trống");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDeskDurationUpdate(Integer deskNumber, Long durationId, DeskDurationRequest request) {
        Map<String, String> errors = new HashMap<>();
        // Kiểm tra desk-duration tồn tại
        Optional<DeskDuration> deskDuration = deskDurationRepository.findByDeskNumberAndDurationId(deskNumber, durationId);
        if (deskDuration.isEmpty()) {
            log.warn("Không tìm thấy desk-duration: {}-{}", deskNumber, durationId);
            throw new ResourceNotFoundException("Không tìm thấy desk-duration");
        }
        // Giá không được để trống
        if (request.getPrice() == null) {
            String field = FieldNameUtil.getFieldName(DeskDurationRequest::getPrice);
            errors.put(field, "Không được để trống");
        }
        if (errors.size() > 0) {
            throw new MultiDuplicateException(errors);
        }
    }

    public void validateDeskDurationStatusUpdate(Integer deskNumber, Long durationId, String status) {
        // Kiểm tra desk-duration tồn tại
        Optional<DeskDuration> deskDuration = deskDurationRepository.findByDeskNumberAndDurationId(deskNumber, durationId);
        if (deskDuration.isEmpty()) {
            log.warn("Không tìm thấy desk-duration: {}-{}", deskNumber, durationId);
            throw new ResourceNotFoundException("Không tìm thấy desk-duration");
        }
        // Kiểm tra status hợp lệ
        DeskDurationStatus deskDurationStatus = DeskDurationStatus.fromCode(status);
        // Kiểm tra trùng lặp trạng thái
        if (deskDurationStatus.equals(deskDuration.get().getStatus())) {
            throw new IllegalArgumentException("Trạng thái đã tồn tại");
        }
    }

    public void validateDeskDurationDelete(Integer deskNumber, Long durationId) {
        // Kiểm tra desk-duration tồn tại
        Optional<DeskDuration> deskDuration = deskDurationRepository.findByDeskNumberAndDurationId(deskNumber, durationId);
        if (deskDuration.isEmpty()) {
            log.warn("Không tìm thấy desk-duration: {}-{}", deskNumber, durationId);
            throw new ResourceNotFoundException("Không tìm thấy desk-duration");
        }
    }

    public void validateDeskDurationSearch(Integer deskNumber, DeskDurationSearchRequest request) {
        // Kiểm tra desk tồn tại
        Optional<Desk> desk = deskRepository.findById(deskNumber);
        if (desk.isEmpty()) {
            log.warn("Không tìm thấy bàn số: {}", deskNumber);
            throw new ResourceNotFoundException("Không tìm thấy bàn");
        }
        // Nếu có trạng thái, kiểm tra trạng thái hợp lệ
        if (request.getStatus() != null) {
            DeskDurationStatus deskDurationStatus = DeskDurationStatus.fromCode(request.getStatus());
        }
    }
}
