package rs.saga.obuka.sagashop.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import rs.saga.obuka.sagashop.audit.Audit;
import rs.saga.obuka.sagashop.dto.audit.AuditDTO;

@Mapper
public interface AuditMapper {
    AuditMapper INSTANCE = Mappers.getMapper( AuditMapper.class );

    default void fillAudit(Audit<Long> audit, @MappingTarget AuditDTO auditDTO){
        if(audit.getCreatedBy() != null){
            auditDTO.setCreatedBy(audit.getCreatedBy().getName() + " " + audit.getCreatedBy().getSurname());
            auditDTO.setLastModifiedBy(audit.getLastModifiedBy().getName() + " " + audit.getLastModifiedBy().getSurname());
            auditDTO.setCreationDate(audit.getCreationDate());
            auditDTO.setLastModifiedDate(audit.getLastModifiedDate());
        }
        auditDTO.setVersion(audit.getVersion());
    }

    default void fillAudit(AuditDTO auditDTO, @MappingTarget Audit<Long> audit){
        audit.setVersion(auditDTO.getVersion());
    }
}