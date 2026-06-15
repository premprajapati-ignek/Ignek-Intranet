package com.ignek.entity.count.configuration;

import aQute.bnd.annotation.metatype.Meta;
import com.ignek.entity.count.constants.EntityCountWebPortletKeys;

@Meta.OCD(id = EntityCountWebPortletKeys.ENTITY_COUNT_WEB)
public interface EntityCountConfiguration {
    @Meta.AD(
            required = false,
            deflt = "employees",
            name = "title"
    )
    public String title();
}