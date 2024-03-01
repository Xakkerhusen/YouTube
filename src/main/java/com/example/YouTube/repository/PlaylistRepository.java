package com.example.YouTube.repository;

import com.example.YouTube.entity.PlaylistEntity;
import com.example.YouTube.mapper.PlayListShortInfoMapper;
import com.example.YouTube.mapper.PlaylistInfoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends CrudRepository<PlaylistEntity, Integer> {


    @Transactional
    @Modifying
    @Query("update PlaylistEntity set visible=false where id=?1")
    void delete(Integer id);

    Page<PlaylistEntity> findAll(Pageable pageable);

    @Query("from PlaylistEntity where visible=true order by orderNumber desc ")
    List<PlaylistEntity> find();

    List<PlaylistEntity> findAllByChannelIdOrderByOrderNumberDesc(String channelId);


    //    @Query("from PlaylistEntity where visible=true ")
    Optional<PlaylistEntity> findByIdAndVisibleTrue(Integer id);

    List<PlaylistEntity> findAllByOrderByOrderNumberDesc();

    @Query(value = "select p.id from channel c" +
            " inner join profile p on p.id = c.profile_id\n" +
            " where c.id=?1",nativeQuery = true)

    Integer findProfile(String channelId);





    @Query(value = "select p.id as playlistId, p.name as playlistName, p.description as playlistDescription, p.status as playlistStatus, p.order_number as playlistOrderNumber, " +
            "c.id as channelId, c.name as channelName, c.photo_id as channelPhotoId, " +
            "pp.id as profileId, pp.name as profileName, pp.surname as profileSurname, pp.main_photo as profilePhotoId " +
            "from playlist p " +
            "inner join channel c on c.id = p.channel_id "+
            "inner join profile pp on pp.id = c.profile_id ",
            countQuery = "select count(p.id) from playlist p " +
                    "inner join channel c on c.id = p.channel_id "+
                    "inner join profile pp on pp.id = c.profile_id " ,
            nativeQuery = true)
    Page<PlaylistInfoMapper> getPlayListInfo( Pageable pageable);

    @Query(value = "select p.id as playlistId, p.name as playlistName, p.description as playlistDescription, p.status as playlistStatus, p.order_number as playlistOrderNumber, " +
            "c.id as channelId, c.name as channelName, c.photo_id as channelPhotoId, " +
            "pp.id as profileId, pp.name as profileName, pp.surname as profileSurname, pp.main_photo as profilePhotoId " +
            "from playlist p " +
            "inner join channel c on c.id = p.channel_id "+
            "inner join profile pp on pp.id = c.profile_id where pp.id=?1",
            nativeQuery = true)
    List<PlaylistInfoMapper> getPlayListInfoList(Integer profileId);


    @Query(value = "select\n" +
            "    pl.id as playlistId,\n" +
            "    pl.name as playlistName,\n" +
            "    pl.created_date as playlistCreatedDate,\n" +
            "    pl.video_count as playlistVideoCount,\n" +
            "    c.id as channelId,\n" +
            "    c.name as channelName,\n" +
            "    (\n" +
            "        select cast(\n" +
            "                       json_agg(\n" +
            "                               json_build_object(\n" +
            "                                       'id', v.id,\n" +
            "                                       'title', v.title,\n" +
            "                                       'duration', v.duration\n" +
            "                               )\n" +
            "                       ) as text\n" +
            "               )\n" +
            "        from video v\n" +
            "                 inner join public.playlist_video pv on v.id = pv.video_id\n" +
            "                 inner join public.playlist p on p.id = pv.playlist_id\n" +
            "        where p.id = pl.id\n" +
            "    ) as playListJson\n" +
            "from playlist pl\n" +
            "         inner join public.channel c on c.id = pl.channel_id\n" +
            "        inner join public.profile p2 on p2.id = c.profile_id\n" +
            "where p2.id=?1\n" +
            "order by pl.created_date desc;",nativeQuery = true)

    List<PlayListShortInfoMapper> getPlayListShortInfoByOwner(Integer profileId);

    @Query(value = "select\n" +
            "    pl.id as playlistId,\n" +
            "    pl.name as playlistName,\n" +
            "    pl.created_date as playlistCreatedDate,\n" +
            "    pl.video_count as playlistVideoCount,\n" +
            "    c.id as channelId,\n" +
            "    c.name as channelName,\n" +
            "    (\n" +
            "        select cast(\n" +
            "                       json_agg(\n" +
            "                               json_build_object(\n" +
            "                                       'id', v.id,\n" +
            "                                       'title', v.title,\n" +
            "                                       'duration', v.duration\n" +
            "                               )\n" +
            "                       ) as text\n" +
            "               )\n" +
            "        from video v\n" +
            "                 inner join public.playlist_video pv on v.id = pv.video_id\n" +
            "                 inner join public.playlist p on p.id = pv.playlist_id\n" +
            "        where p.id = pl.id\n" +
            "    ) as playListJson\n" +
            "from playlist pl\n" +
            "         inner join public.channel c on c.id = pl.channel_id\n" +
            "        inner join public.profile p2 on p2.id = c.profile_id\n" +
            "where c.id=?1\n" +
            "order by pl.created_date desc;",nativeQuery = true)

    List<PlayListShortInfoMapper> getPlayListShortInfoByChannel(String channelId);
//    id, name,created_date,channel(id,name),video_count,video_list[{id,name,duration}]


public interface PlaylistRepository extends CrudRepository<PlaylistEntity, Integer> {



}
