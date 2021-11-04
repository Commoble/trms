drop table if exists
	request_attachments,
	approvals,
	reimbursement_requests,
	grading_formats,
	event_types,
	employee_role_junctions,
	roles,
	department_heads,
	employees,
	departments;


create table if not exists departments(
	department_id serial primary key,
	name varchar(100) not null
);

create table if not exists employees(
	employee_id serial primary key,
	name varchar(50) not null,
	username varchar(50) not null,
	password varchar(50) not null,
	supervisor_id int default null references employees(employee_id) on delete set null,
	department_id int default null references departments(department_id) on delete set null,
	check(supervisor_id != employee_id) -- employee can't supervise themself, should be null if no supervisor
);

-- this table is separate from departments to keep departments,
-- because departments and employees cannot reference each other
create table if not exists department_heads(
	department_id int unique not null references departments(department_id) on delete cascade,
	department_head_employee_id int default null references employees(employee_id) on delete set null
);

create table if not exists roles(
	role_id serial primary key,
	name varchar(50) not null,
	can_coordinate_benefits bool not null default false
);

create table if not exists employee_role_junctions(
	employee_id int not null references employees(employee_id) on delete cascade,
	role_id int not null references roles(role_id) on delete cascade,
	unique(employee_id, role_id) -- avoid duplicate tuples, table should be setlike
);

create table if not exists event_types(
	event_type_id serial primary key,
	name varchar(50) not null,
	coverage_percentage int not null check(coverage_percentage >= 0 and coverage_percentage <= 100)
);

create table if not exists grading_formats(
	format_id serial primary key,
	name varchar(50) not null,
	default_passing_grade varchar(20) not null,
	maximum_grade varchar(20) not null,
	requires_presentation bool not null default false
);

create table if not exists reimbursement_requests(
	request_id serial primary key,
	employee_id int not null references employees(employee_id) on delete cascade,
	awarded bool not null default false, -- set to true when reimbursement is paid out
	event_type int not null default 1 references event_types(event_type_id) on delete restrict,
	event_date bigint, -- can be null if no specific date is required
	event_address varchar(100) not null, -- might want to refactor into multiple address fields
	event_description varchar(500) not null,
	event_cost int not null check (event_cost > 0), -- if it's 0 then they dont need approval
	reimbursement_amount int not null check(reimbursement_amount >= 0),
	excessive_reimbursement int not null check (excessive_reimbursement >= 0), -- this is raw data because "the amount that was available to the employee at the time of the request" might change later due to denied or cancelled requests
	grading_format int not null references grading_formats(format_id) on delete restrict,
	passing_grade varchar(20) not null, -- web app can suggest the default, service can validate
	work_related_justification varchar(500) not null,
	submission_time bigint not null,
	requestor_must_review_modifications bool not null default false,
	work_time_missed varchar(500) not null -- optional but they can leave it blank (empty string)
);

create table if not exists request_attachments(
	attachment_id serial primary key,
	request_id int references reimbursement_requests(request_id) on delete set null,
	description varchar(500) not null,
	location varchar(500) not null, -- indicates a file download location of some kind
	approval_level int check(approval_level in (null,1,2)), -- indicates email approval
	proof_of_completion bool not null default false -- attachment is a grade certificate or presentation media
);

create table if not exists approvals(
	approval_id serial primary key,
	approver_id int not null references employees on delete cascade,
	request_id int not null references reimbursement_requests on delete restrict,
	approval_level int not null check(approval_level in (1,2,3)), -- highest level of approver -- 1 for supervisor, 2 dept, 3 benco
	approved boolean not null, -- false=denied, true=approved
	time bigint not null,
	comments varchar(500) not null
);

insert into departments values
	(default, 'Administration'),
	(default, 'Accounting'),
	(default, 'Human Resources'),
	(default, 'Marketing'),
	(default, 'Research and Development');
	
insert into roles values
	(default, 'Benefits Coordinator', true);
	
insert into employees values (default, 'Jim CEOPerson', 'jimceoperson', 'my cake is full of termites', null, 1);
insert into employees values (default, 'Alice Accountinghead', 'aliceaccountinghead', 'my cake is full of termites', 1, 2);
insert into employees values (default, 'Henry Humanresourceshead', 'henryhumanresourceshead', 'my cake is full of termites', 1, 3);
insert into employees values (default, 'Martha Marketinghead', 'marthamarketinghead', 'my cake is full of termites', 1, 4);
insert into employees values (default, 'Richard Researchhead', 'richardresearchhead', 'my cake is full of termites', 1, 5);
insert into employees values (default, 'Larry Accountingmanager', 'larryaccountingmanager', 'my cake is full of termites', 2, 2);
insert into employees values (default, 'Steve Regularaccountant', 'steveregularaccountant', 'my cake is full of termites', 6, 2);
insert into employees values (default, 'Tim Humanresourcemanager', 'timhumanresourcemanager', 'my cake is full of termites', 3, 3);
insert into employees values (default, 'Breanna Benefitscoordinator', 'breannabenefitscoordinator', 'my cake is full of termites', 8, 3);

insert into employee_role_junctions values (9, 1);

insert into department_heads values
	(1,1),
	(2,2),
	(3,3),
	(4,4),
	(5,5);

insert into event_types values
	(default, 'Other', 30),
	(default, 'University Courses', 80),
	(default, 'Seminars', 60),
	(default, 'Certification Preparation Classes', 75),
	(default, 'Certification', 100),
	(default, 'Technical Training', 90);

insert into grading_formats values
	(default, 'Pass/Fail', 'Pass', 'Pass', false),
	(default, 'Percentage', '80', '100', false),
	(default, 'Letter', 'C+', 'A', false),
	(default, 'GPA', '3.0', '4.0', false),
	(default, 'Pass/Fail Presentation', 'Pass', 'Pass', true);
	
