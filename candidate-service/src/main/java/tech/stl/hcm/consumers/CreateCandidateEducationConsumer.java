package tech.stl.hcm.consumers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import tech.stl.hcm.candidate.service.CandidateEducationService;
import tech.stl.hcm.common.dto.CandidateEducationDTO;
import tech.stl.hcm.message.broker.consumer.MessageHandler;
import tech.stl.hcm.message.broker.consumer.TopicListener;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
@TopicListener(
        topic = "${candidateeducation.kafka.topic}",
        groupId = "${candidateeducation.kafka.group-id}",
        valueType = CandidateEducationDTO.class,
        enableProperty = "candidateeducation.kafka.enable"
)
public class CreateCandidateEducationConsumer implements MessageHandler<CandidateEducationDTO> {

    private final CandidateEducationService candidateEducationService;

    @Override
    public void handle(CandidateEducationDTO candidateEducation, String key) {
        try {
            String candidateId = candidateEducation.getCandidateId().toString();
            MDC.put("candidateId", candidateId);
            log.info("Consumed candidate education for candidate: {}", candidateEducation.getCandidateId());
            candidateEducationService.createCandidateEducation(UUID.fromString(candidateId), candidateEducation);
        } finally {
            MDC.clear();
        }
    }
} 