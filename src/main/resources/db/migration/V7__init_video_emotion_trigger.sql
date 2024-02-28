CREATE OR REPLACE FUNCTION video_status_count_trigger_function()
RETURNS TRIGGER
LANGUAGE PLPGSQL
AS
$$
BEGIN
  if TG_OP = 'INSERT'  then
    if NEW.status = 'LIKE'  then
update video set like_count = like_count + 1 where id = NEW.video_id;
return NEW;
else
update video set dislike_count = dislike_count + 1 where id = NEW.video_id;
return NEW;
end if;
  elseif TG_OP = 'DELETE' then
     if OLD.status = 'LIKE'  then
update video set like_count = like_count - 1 where id = OLD.video_id;
return OLD;
else
update video set dislike_count = dislike_count - 1 where id = OLD.video_id;
return OLD;
end if;
  elseif TG_OP = 'UPDATE' then
      -- OLD
      if OLD.status = 'LIKE'  then
update video set like_count = like_count - 1 where id = OLD.video_id;
else
update video set dislike_count = dislike_count - 1 where id = OLD.video_id;
end if;
    -- NEW
    if NEW.status = 'LIKE'  then
update video set like_count = like_count + 1 where id = NEW.video_id;
else
update video set dislike_count = dislike_count + 1 where id = NEW.video_id;
end if;
return NEW;
end if;
END;$$;

CREATE TRIGGER video_status_count_trigger
BEFORE INSERT OR UPDATE OR DELETE
ON video_emotion
FOR EACH ROW
EXECUTE PROCEDURE video_status_count_trigger_function();
