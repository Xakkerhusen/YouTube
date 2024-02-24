CREATE
OR REPLACE FUNCTION article_status_count_trigger_function()
    RETURNS TRIGGER
    LANGUAGE PLPGSQL
AS
$$
BEGIN
    if
TG_OP = 'INSERT'  then
        if NEW.type = 'LIKE'  then
update comment
set like_count = like_count + 1
where id = NEW.comment_id;
return NEW;
else
update comment
set dislike_count = dislike_count + 1
where id = NEW.comment_id;
return NEW;
end if;
    elseif
TG_OP = 'DELETE' then
        if OLD.type = 'LIKE'  then
update comment
set like_count = like_count - 1
where id = OLD.comment_id;
return OLD;
else
update comment
set dislike_count = dislike_count - 1
where id = OLD.comment_id;
return OLD;
end if;
    elseif
TG_OP = 'UPDATE' then
        -- OLD
        if OLD.type = 'LIKE'  then
update comment
set like_count = like_count - 1
where id = OLD.comment_id;
else
update comment
set dislike_count = dislike_count - 1
where id = OLD.comment_id;
end if;
        -- NEW
        if
NEW.type = 'LIKE'  then
update comment
set like_count = like_count + 1
where id = NEW.comment_id;
else
update comment
set dislike_count = dislike_count + 1
where id = NEW.comment_id;
end if;
return NEW;
end if;
END;$$;

CREATE TRIGGER article_status_count_trigger
    BEFORE INSERT OR
UPDATE OR
DELETE
ON comment_like
    FOR EACH ROW
EXECUTE PROCEDURE article_status_count_trigger_function();