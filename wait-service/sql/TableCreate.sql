
create table wait_room_complete (
    wait_room_complete_id bigint not null,
    created_at timestamp(6),
    modified_at timestamp(6),
    host_id varchar(255),
    host_name varchar(255),
    limit_members integer not null,
    room_name varchar(255),
    wait_room_id varchar(255),
    wait_room_status varchar(255),
    primary key (wait_room_complete_id)
);

create table wait_room_complete_join_member (
    id bigint not null,
    created_at timestamp(6),
    modified_at timestamp(6),
    user_id varchar(255),
    wait_room_complete_id bigint,
    primary key (id)
);

create index wait_room_index on wait_room_complete (wait_room_id);
create sequence wait_room_complete_join_member_seq start with 1 increment by 50;

alter table if exists wait_room_complete_join_member
    add constraint FKcgk8vlfw3txcsoevx5u76vo3q
    foreign key (wait_room_complete_id)
    references wait_room_complete;