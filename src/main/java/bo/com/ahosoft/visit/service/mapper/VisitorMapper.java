package bo.com.ahosoft.visit.service.mapper;


import bo.com.ahosoft.visit.domain.*;
import bo.com.ahosoft.visit.service.dto.VisitorDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Visitor} and its DTO {@link VisitorDTO}.
 */
@Mapper(componentModel = "spring", uses = {DocumentTypeMapper.class})
public interface VisitorMapper extends EntityMapper<VisitorDTO, Visitor> {

    @Mapping(source = "documentType.id", target = "documentTypeId")
    VisitorDTO toDto(Visitor visitor);

    @Mapping(source = "documentTypeId", target = "documentType")
    Visitor toEntity(VisitorDTO visitorDTO);

    default Visitor fromId(Long id) {
        if (id == null) {
            return null;
        }
        Visitor visitor = new Visitor();
        visitor.setId(id);
        return visitor;
    }
}
