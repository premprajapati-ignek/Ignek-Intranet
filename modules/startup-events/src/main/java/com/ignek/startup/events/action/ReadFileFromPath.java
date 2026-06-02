package com.ignek.startup.events.action;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.InputStream;

public class ReadFileFromPath {
    private static final Log log = LogFactoryUtil.getLog(ReadFileFromPath.class);
    public String getFile(String path) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
            if (is == null) {
                log.error("Resource not found on classpath: " + path);
                return StringPool.BLANK;
            }
            return new String(is.readAllBytes());
        } catch (Exception e) {
            log.error("Unable to read file: " + path, e);
            return StringPool.BLANK;
        }
    }
}
