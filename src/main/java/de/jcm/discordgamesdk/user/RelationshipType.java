package de.jcm.discordgamesdk.user;

import de.jcm.discordgamesdk.user.Relationship;

/**
 * The type of a {@link Relationship} between two users.
 * @see <a href="https://discordapp.com/developers/docs/game-sdk/relationships#data-models-relationshiptype-enum">
 *     https://discordapp.com/developers/docs/game-sdk/relationships#data-models-relationshiptype-enum</a>
 */
public enum RelationshipType
{
	/**
	 * There is no special relationship between the users.
	 */
	NONE,
	/**
	 * The users are friends. &lt;3
	 */
	FRIEND,
	/**
	 * The user is blocked by the current user.
	 */
	BLOCKED,
	/**
	 * The current user has received a friend request from the user. :O
	 */
	PENDING_INCOMING,
	/**
	 * The current user has sent a friend request to the user.
	 */
	PENDING_OUTGOING,
	/**
	 * The users have no special relationship, but have interacted frequently and recently.
	 */
	IMPLICIT
}
