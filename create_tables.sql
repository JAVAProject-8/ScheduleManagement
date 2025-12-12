CREATE TABLE users (
    user_id varchar(20) NOT NULL,
    password varchar(100) NOT NULL,
    name varchar(30) NOT NULL,
    organization varchar(50) DEFAULT NULL,
    birth_date date NOT NULL,
    phone varchar(15) NOT NULL,
    email varchar(100) NOT NULL,
    PRIMARY KEY (user_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE user_groups (
    group_id varchar(20) NOT NULL,
    group_name varchar(20) DEFAULT NULL,
    invite_code char(6) DEFAULT NULL,
    PRIMARY KEY (group_id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE group_members (
    user_id varchar(20) NOT NULL,
    group_id varchar(20) NOT NULL,
    is_admin char(1) NOT NULL DEFAULT 'N',
    task varchar(100) DEFAULT NULL,
    PRIMARY KEY (user_id, group_id),
    KEY fk_group_members_groups (group_id),
    CONSTRAINT fk_group_members_groups FOREIGN KEY (group_id) REFERENCES user_groups (group_id) ON DELETE CASCADE,
    CONSTRAINT fk_group_members_users FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE schedules (
    schedule_id int NOT NULL AUTO_INCREMENT,
    writer_id varchar(20) NOT NULL,
    group_id varchar(20) DEFAULT NULL,
    schedule_description varchar(50) NOT NULL,
    schedule_type varchar(10) NULL,
    start_at datetime NOT NULL,
    end_at datetime NOT NULL,
    PRIMARY KEY (schedule_id),
    KEY fk_schedules_users (writer_id),
    KEY fk_schedules_groups (group_id),
    CONSTRAINT fk_schedules_groups FOREIGN KEY (group_id) REFERENCES user_groups (group_id) ON DELETE CASCADE,
    CONSTRAINT fk_schedules_users FOREIGN KEY (writer_id) REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE memos (
    memo_id int NOT NULL AUTO_INCREMENT,
    group_id varchar(20) NOT NULL,
    writer_id varchar(20) NOT NULL,
    content text NOT NULL,
    created_at datetime NOT NULL,
    PRIMARY KEY (memo_id),
    KEY fk_memoes_groups (group_id),
    KEY fk_memoes_users (writer_id),
    CONSTRAINT fk_memoes_groups FOREIGN KEY (group_id) REFERENCES user_groups (group_id) ON DELETE CASCADE,
    CONSTRAINT fk_memoes_users FOREIGN KEY (writer_id) REFERENCES users (user_id) ON DELETE CASCADE
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;