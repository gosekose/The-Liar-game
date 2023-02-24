create table game_result (
                             game_result_id varchar(255) not null,
                             created_at timestamp(6),
                             modified_at timestamp(6),
                             game_id varchar(255),
                             game_name varchar(255),
                             host_id varchar(255),
                             room_id varchar(255),
                             total_users integer not null,
                             winner smallint,
                             topic_id bigint,
                             primary key (game_result_id)
);

create table player (
                        player_id varchar(255) not null,
                        created_at timestamp(6),
                        modified_at timestamp(6),
                        exp bigint,
                        level smallint,
                        loses bigint,
                        total_games bigint,
                        visible_game_result boolean not null,
                        wins bigint,
                        member_id bigint,
                        primary key (player_id)
);

create table player_result (
                               player_result_id varchar(255) not null,
                               created_at timestamp(6),
                               modified_at timestamp(6),
                               answers boolean,
                               exp bigint,
                               game_role smallint,
                               is_win boolean,
                               user_id varchar(255),
                               game_result_id varchar(255),
                               primary key (player_result_id)
);

create index game_result_game_id_index on game_result (game_id);
create index player_exp_index on player (exp);
create index player_member_index on player (member_id);
create index player_result_user_id_index on player_result (user_id);

alter table if exists game_result
    add constraint FKj91wwh8njkr1qv9mvealyi9g
    foreign key (topic_id)
    references topic;


alter table if exists player
    add constraint FKtky0mr5eq3kww4rqiscxivebo
    foreign key (member_id)
    references member;

alter table if exists player_result
    add constraint FK36upu3v8txacb8n97pglfe6kk
    foreign key (game_result_id)
    references game_result;