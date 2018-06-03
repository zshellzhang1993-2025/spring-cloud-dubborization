package cc.zshell.eureka.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class JsonUtil {

    private final static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private final static ObjectMapper objectMapper = new ObjectMapper();

    static {
        SimpleModule simpleModule = new SimpleModule("SimpleJodaModule", new Version(1, 0, 0, null, null, null));
        objectMapper.registerModule(simpleModule);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_NUMERIC_LEADING_ZEROS, true);
    }

    public static String encode(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            logger.error("jackson encode error:", e); //$NON-NLS-1$
        }
        return null;
    }

    public static <T> T decode(String json, Class<T> valueType) {
        try {
            return objectMapper.readValue(json, valueType);
        } catch (Exception e) {
            logger.error("jackson decodeMap(String, Class<T>) error: ", e);
        }
        return null;
    }

    public static <T> T decode(String json, TypeReference typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            logger.error("jackson decode(String, TypeReference) error: ", e);
        }
        return null;
    }

    public static <T> List<T> decodeList(String json, Class<T> c) {
        List<T> t = null;
        try {
            if (json != null) {
                t = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, c));
            }
        } catch (Exception e) {
            logger.error("decodeList error", e);
        }
        return t;
    }

    public static <K, V> List<Map<K, V>> decodeList(String json, TypeReference<List<Map<K, V>>> ref) {
        try {
            return objectMapper.readValue(json, ref);
        } catch (Exception e) {
            logger.error("jackson decodeList(String, TypeReference<T>) error: ", e);
        }
        return null;
    }

    public static <K, V> Map<K, V> decodeMap(String json, Class<K> key, Class<V> value) {
        Map<K, V> map = null;
        try {
            if (json != null) {
                map = objectMapper.readValue(json, objectMapper.getTypeFactory().constructMapType(Map.class, key, value));
            }
        } catch (Exception e) {
            logger.error("decodeList error", e);
        }
        return map;
    }

    public static <K, V> Map<K, V> decodeMap(String json, TypeReference<Map<K, V>> ref) {
        try {
            return objectMapper.readValue(json, ref);
        } catch (Exception e) {
            logger.error("jackson decodeMap(String, TypeReference<T>) error: ", e);
        }
        return null;
    }

    public static <K, V> List<Map<K, V>> decodeListOrMap(String json, K k, V v) {


        // try list
        List<Map<K, V>> list = decodeList(json, new TypeReference<List<Map<K, V>>>() {
        });
        if (list == null) {
            // try map
            Map<K, V> map = decodeMap(json, new TypeReference<Map<K, V>>() {
            });
            if (map == null) {
                logger.error("jackson decodeListOrMap final fail:{}", json);
            } else {
                list = Lists.newArrayList(map);
                return list;
            }
        } else {
            return list;
        }
        return null;
    }

    public static JsonNode readTree(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
            logger.error("jackson read tree error:{}", json);
            return null;
        }
    }

}
