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

CREATE SEQUENCE broadcasts_id_seq START 1;
CREATE TABLE "public"."broadcasts" (
    "id" int4 NOT NULL DEFAULT nextval('broadcasts_id_seq'::regclass),
    "type" int4,
    "number" varchar(10),
    "subject" varchar(255) COLLATE "pg_catalog"."default",
    "description" varchar(1024) COLLATE "pg_catalog"."default",
    "lecturer" varchar(255) COLLATE "pg_catalog"."default",
    "attendees" varchar(512) COLLATE "pg_catalog"."default",
    "starttime" int8,
    CONSTRAINT "broadcasts_pkey" PRIMARY KEY ("id")
);

CREATE SEQUENCE broadcastdevices_id_seq START 1;
CREATE TABLE "public"."broadcastdevices" (
    "id" int4 NOT NULL DEFAULT nextval('broadcastdevices_id_seq'::regclass),
    "broadcastid" int4 NOT NULL,
    "deviceid" int4 NOT NULL,
    CONSTRAINT "broadcastdevices_pkey" PRIMARY KEY ("id")
);
