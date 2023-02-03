create table wait_room_complete (
    wait_room_complete_id bigint not null,
    created_at timestamp(6),
    host_id varchar(255),
    host_name varchar(255),
    limit_members integer not null,
    modified_at timestamp(6),
    room_name varchar(255),
    wait_room_id varchar(255),
    primary key (wait_room_complete_id)
)

create table wait_room_join_member (
   id bigint not null,
   created_at timestamp(6),
   modified_at timestamp(6),
   user_id varchar(255),
   wait_room_complete_id bigint,
   primary key (id)
)

alter table if exists wait_room_join_member
    add constraint FK1xvnkpgwqkkv9dlju76n2fsuc
    foreign key (wait_room_complete_id)
    references wait_room_complete

create index wait_room_index on wait_room_complete (wait_room_id)