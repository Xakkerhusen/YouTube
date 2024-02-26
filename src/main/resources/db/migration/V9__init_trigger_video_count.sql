CREATE OR REPLACE FUNCTION update_video_count()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
UPDATE playlist SET video_count = video_count + 1 WHERE id = NEW.playlist_id;
ELSIF TG_OP = 'DELETE' THEN
UPDATE playlist SET video_count = video_count - 1 WHERE id = OLD.playlist_id;
END IF;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER update_video_count_trigger
    AFTER INSERT OR DELETE ON playlist_video
FOR EACH ROW
EXECUTE FUNCTION update_video_count();
