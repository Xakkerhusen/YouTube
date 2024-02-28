CREATE OR REPLACE FUNCTION video_watch_count_trigger_function()
RETURNS TRIGGER
LANGUAGE PLPGSQL
AS
$$
BEGIN
  if TG_OP = 'INSERT'  then
update video set view_count = view_count + 1 where id = NEW.video_id;
return NEW;
end if;
return NEW;
END;$$;

CREATE TRIGGER video_watch_count_trigger
BEFORE INSERT
ON video_watch
FOR EACH ROW
EXECUTE PROCEDURE video_watch_count_trigger_function();
