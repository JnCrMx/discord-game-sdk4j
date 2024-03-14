package de.jcm.discordgamesdk;

import de.jcm.discordgamesdk.user.DiscordUser;
import de.jcm.discordgamesdk.user.Relationship;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A {@link DiscordEventAdapter} that forwards all events to other adapters.
 * <p>
 * All events are unconditionally forwarded to other {@link DiscordEventAdapter}s
 * that can be registered and unregistered dynamically.
 * <p>
 * A {@link CopyOnWriteArrayList} is used for increased thread-safety.
 * @see DiscordEventAdapter
 * @see #addListener(DiscordEventAdapter)
 * @see #removeListener(DiscordEventAdapter)
 * @see #removeAllListeners()
 */
public class DiscordEventHandler extends DiscordEventAdapter
{
	private final List<DiscordEventAdapter> listeners = new CopyOnWriteArrayList<>();

	/**
	 * Registers a new event adapter to forward Discord events to.
	 * @param listener Any kind of {@link DiscordEventAdapter}
	 * @see #removeListener(DiscordEventAdapter)
	 * @see CopyOnWriteArrayList#add(Object)
	 */
	public void addListener(DiscordEventAdapter listener)
	{
		this.listeners.add(listener);
	}

	/**
	 * Unregisters a event adapter that has been registered before.
	 * @param listener Any kind of {@link DiscordEventAdapter} that has been added before
	 * @return {@code true} if the adapter was registered and is now unregisters
	 * @see #addListener(DiscordEventAdapter)
	 * @see CopyOnWriteArrayList#remove(Object)
	 */
	public boolean removeListener(DiscordEventAdapter listener)
	{
		return this.listeners.remove(listener);
	}

	/**
	 * Removes <b>all</b> registered event adapters.
	 * <p>
	 * Until new event adapters are registered, all events are effectively ignored.
	 * @see CopyOnWriteArrayList#clear()
	 */
	public void removeAllListeners()
	{
		this.listeners.clear();
	}

	@Override
	public void onActivityJoin(String secret)
	{
		listeners.forEach(l->l.onActivityJoin(secret));
	}

	@Override
	public void onActivitySpectate(String secret)
	{
		listeners.forEach(l->l.onActivitySpectate(secret));
	}

	@Override
	public void onActivityJoinRequest(DiscordUser user)
	{
		listeners.forEach(l->l.onActivityJoinRequest(user));
	}

	@Override
	public void onCurrentUserUpdate()
	{
		listeners.forEach(DiscordEventAdapter::onCurrentUserUpdate);
	}

	@Override
	public void onOverlayToggle(boolean locked)
	{
		listeners.forEach(l->l.onOverlayToggle(locked));
	}

	@Override
	public void onRelationshipRefresh()
	{
		listeners.forEach(DiscordEventAdapter::onRelationshipRefresh);
	}

	@Override
	public void onRelationshipUpdate(Relationship relationship)
	{
		listeners.forEach(l->l.onRelationshipUpdate(relationship));
	}
}
