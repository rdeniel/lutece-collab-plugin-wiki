/*
 * Copyright (c) 2002-2018, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.wiki.business;

import fr.paris.lutece.portal.service.plugin.Plugin;
import fr.paris.lutece.util.sql.DAOUtil;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class provides Data Access methods for Topic objects
 */
public final class TopicDAO implements ITopicDAO
{
    // Constants
    private static final String SQL_QUERY_NEW_PK = "SELECT max( id_topic ) FROM wiki_topic";
    private static final String SQL_QUERY_SELECT = "SELECT id_topic, namespace, page_name, page_view_role, page_edit_role FROM wiki_topic WHERE id_topic = ?";
    private static final String SQL_QUERY_INSERT = "INSERT INTO wiki_topic ( id_topic, namespace, page_name, page_view_role, page_edit_role ) VALUES ( ?, ?, ?, ?, ? ) ";
    private static final String SQL_QUERY_DELETE = "DELETE FROM wiki_topic WHERE id_topic = ? ";
    private static final String SQL_QUERY_UPDATE = "UPDATE wiki_topic SET id_topic = ?, namespace = ?, page_name = ?, page_view_role = ?, page_edit_role = ? WHERE id_topic = ?";
    private static final String SQL_QUERY_SELECTALL = "SELECT id_topic, namespace, page_name, page_view_role, page_edit_role FROM wiki_topic";
    private static final String SQL_QUERY_SELECT_BY_NAME = "SELECT id_topic, namespace, page_name, page_view_role, page_edit_role FROM wiki_topic WHERE page_name  = ?";

    /**
     * Generates a new primary key
     * 
     * @param plugin
     *            The Plugin
     * @return The new primary key
     */
    public int newPrimaryKey( Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_NEW_PK, plugin );
        daoUtil.executeQuery( );

        int nKey;

        daoUtil.next( );
        nKey = daoUtil.getInt( 1 ) + 1;
        daoUtil.free( );

        return nKey;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void insert( Topic topic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_INSERT, plugin );

        topic.setIdTopic( newPrimaryKey( plugin ) );

        daoUtil.setInt( 1, topic.getIdTopic( ) );
        daoUtil.setInt( 2, topic.getNamespace( ) );
        daoUtil.setString( 3, topic.getPageName( ) );
        daoUtil.setString( 4, topic.getViewRole( ) );
        daoUtil.setString( 5, topic.getEditRole( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Topic load( int nId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT, plugin );
        daoUtil.setInt( 1, nId );
        daoUtil.executeQuery( );

        Topic topic = null;

        if ( daoUtil.next( ) )
        {
            topic = new Topic( );

            topic.setIdTopic( daoUtil.getInt( 1 ) );
            topic.setNamespace( daoUtil.getInt( 2 ) );
            topic.setPageName( daoUtil.getString( 3 ) );
            topic.setViewRole( daoUtil.getString( 4 ) );
            topic.setEditRole( daoUtil.getString( 5 ) );
        }

        daoUtil.free( );

        return topic;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void delete( int nTopicId, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_DELETE, plugin );
        daoUtil.setInt( 1, nTopicId );
        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public void store( Topic topic, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_UPDATE, plugin );

        daoUtil.setInt( 1, topic.getIdTopic( ) );
        daoUtil.setInt( 2, topic.getNamespace( ) );
        daoUtil.setString( 3, topic.getPageName( ) );
        daoUtil.setString( 4, topic.getViewRole( ) );
        daoUtil.setString( 5, topic.getEditRole( ) );
        daoUtil.setInt( 6, topic.getIdTopic( ) );

        daoUtil.executeUpdate( );
        daoUtil.free( );
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Collection<Topic> selectTopicsList( Plugin plugin )
    {
        Collection<Topic> topicList = new ArrayList<Topic>( );
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECTALL, plugin );
        daoUtil.executeQuery( );

        while ( daoUtil.next( ) )
        {
            Topic topic = new Topic( );

            topic.setIdTopic( daoUtil.getInt( 1 ) );
            topic.setNamespace( daoUtil.getInt( 2 ) );
            topic.setPageName( daoUtil.getString( 3 ) );
            topic.setViewRole( daoUtil.getString( 4 ) );
            topic.setEditRole( daoUtil.getString( 5 ) );

            topicList.add( topic );
        }

        daoUtil.free( );

        return topicList;
    }

    /**
     * {@inheritDoc }
     */
    @Override
    public Topic load( String strTopicName, Plugin plugin )
    {
        DAOUtil daoUtil = new DAOUtil( SQL_QUERY_SELECT_BY_NAME, plugin );
        daoUtil.setString( 1, strTopicName );
        daoUtil.executeQuery( );

        Topic topic = null;

        if ( daoUtil.next( ) )
        {
            topic = new Topic( );

            topic.setIdTopic( daoUtil.getInt( 1 ) );
            topic.setNamespace( daoUtil.getInt( 2 ) );
            topic.setPageName( daoUtil.getString( 3 ) );
            topic.setViewRole( daoUtil.getString( 4 ) );
            topic.setEditRole( daoUtil.getString( 5 ) );
        }

        daoUtil.free( );

        return topic;
    }
}
