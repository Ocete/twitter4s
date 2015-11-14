package com.danielasfregola.twitter4s.http.clients.followers

import scala.concurrent.Future

import com.danielasfregola.twitter4s.entities.{Users, UserStringifiedIds, UserIds}
import com.danielasfregola.twitter4s.http.clients.OAuthClient
import com.danielasfregola.twitter4s.http.clients.followers.parameters.{FollowersParameters, FollowingParameters}
import com.danielasfregola.twitter4s.util.Configurations

trait TwitterFollowerClient extends OAuthClient with Configurations {

  private val followersUrl = s"$apiTwitterUrl/$twitterVersion/followers"

  def followersIdsForUserId(user_id: Long, cursor: Long = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = false)
    genericFollowersIds[UserIds](parameters)
  }

  def followersIds(screen_name: String, cursor: Long = -1, count: Int = -1): Future[UserIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = false)
    genericFollowersIds[UserIds](parameters)
  }

  def followersIdsForUserIdStringified(user_id: Long, cursor: Long = -1, count: Int = -1): Future[UserStringifiedIds] = {
    val parameters = FollowingParameters(Some(user_id), screen_name = None, cursor, count, stringify_ids = true)
    genericFollowersIds[UserStringifiedIds](parameters)
  }

  def followersIdsStringified(screen_name: String, cursor: Long = -1, count: Int = -1): Future[UserStringifiedIds] = {
    val parameters = FollowingParameters(user_id = None, Some(screen_name), cursor, count, stringify_ids = true)
    genericFollowersIds[UserStringifiedIds](parameters)
  }

  private def genericFollowersIds[T: Manifest](parameters: FollowingParameters): Future[T] =
    Get(s"$followersUrl/ids.json", parameters).respondAs[T]

  def followers(screen_name: String,
                cursor: Long = -1,
                count: Int = -1,
                skip_status: Boolean = false,
                include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FollowersParameters(user_id = None, screen_name = Some(screen_name), cursor, count, skip_status, include_user_entities)
    genericFollowers(parameters)
  }

  def followersForUserId(user_id: Long,
                        cursor: Long = -1,
                        count: Int = -1,
                        skip_status: Boolean = false,
                        include_user_entities: Boolean = true): Future[Users] = {
    val parameters = FollowersParameters(user_id = Some(user_id), screen_name = None, cursor, count, skip_status, include_user_entities)
    genericFollowers(parameters)
  }

  private def genericFollowers(parameters: FollowersParameters): Future[Users] =
    Get(s"$followersUrl/list.json", parameters).respondAs[Users]
}