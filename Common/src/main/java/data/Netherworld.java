package data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author mads1
 */
public class Netherworld {

    private final Map<String, Entity> deathMap = new ConcurrentHashMap<>();

    public String addEntity(Entity entity) {
        deathMap.put(entity.getID(), entity);
        return entity.getID();
    }

    public void removeEntity(String entityID) {
        deathMap.remove(entityID);
    }

    public void removeEntity(Entity entity) {
        deathMap.remove(entity.getID());
    }

    public Collection<Entity> getEntities() {
        return deathMap.values();
    }

    public List<Entity> getEntities(EntityType... entityTypes) {
        List<Entity> r = new ArrayList<>();
        for (Entity e : getEntities()) {
            for (EntityType entityType : entityTypes) {
                if (entityType.equals(e.getType())) {
                    r.add(e);
                }
            }
        }
        return r;
    }
}
