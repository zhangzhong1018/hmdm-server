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

ALTER TABLE "public"."devices" ADD COLUMN locationid int4;

ALTER TABLE "public"."userrolesettings" ADD COLUMN columndisplayeddevicelocation bool;
