package ac.bris.cs.platformer.theGame.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Maintains a list of entities that is sorted by zPosition on each add
 *
 * Note: I realise that sorting on each add is not a particularly efficient
 * way of doing this, and this would be a later target for optimisation.
 *
 * At this stage, however adding to the list will only occur when the game starts
 * up and for a comparatively small number of entities.
 */
public class EntityList implements Iterable<Entity> {

   private final List<Entity> entities;

   public EntityList()
   {
      entities = new ArrayList<>();
   }

   public void add(final Entity entity)
   {
      entities.add(entity);
      Collections.sort(entities);
   }

   public boolean removeAll(final Collection<Entity> remove)
   {
      return entities.removeAll(remove);
   }

   public boolean addAll(final Collection<Entity> add)
   {
      final boolean returnVal = entities.addAll(add);
      Collections.sort(entities);
      return returnVal;
   }

   public Entity get(final int index)
   {
      return entities.get(index);
   }

   public void clear()
   {
      entities.clear();
   }

   public int size()
   {
      return entities.size();
   }

   @Override
   public Iterator<Entity> iterator()
   {
      return entities.iterator();
   }
}
