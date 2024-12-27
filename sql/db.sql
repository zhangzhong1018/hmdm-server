-- CREATE SEQUENCE usergroups_id_seq START 1;
-- CREATE TABLE "public"."usergroups" (
--     "id" int4 NOT NULL DEFAULT nextval('usergroups_id_seq'::regclass),
--     "userid" int4 NOT NULL,
--     "groupid" int4 NOT NULL,
--     CONSTRAINT "usergroups_pkey" PRIMARY KEY ("id")
-- );
-- ALTER TABLE "public"."usergroups" OWNER TO "hmdm";

CREATE SEQUENCE locations_id_seq START 1;
CREATE TABLE "public"."locations" (
    "id" int4 NOT NULL DEFAULT nextval('locations_id_seq'::regclass),
    "code" varchar(10) COLLATE "pg_catalog"."default",
    "name" varchar(255) COLLATE "pg_catalog"."default",
    "state" varchar(255) COLLATE "pg_catalog"."default",
    "latitude" numeric(10,2),
    "longitude" numeric(10,2),
    CONSTRAINT "locations_pkey" PRIMARY KEY ("id")
);
ALTER TABLE "public"."locations" OWNER TO "hmdm";
