CREATE TABLE pessoa (
    codigo BIGINT(20) PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(50) NOT NULL,
    logradouro VARCHAR(30),
    numero VARCHAR(30),
    complemento VARCHAR(30),
    bairro VARCHAR(30),
    cep VARCHAR(30),
    cidade VARCHAR(30),
    estado VARCHAR(30),
    ativo BOOLEAN NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Bob Esponja', 'Rua do Abacaxi', '10', null, 'Fenda do Biquíni', '38.400-121', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Patrick Estrela', 'Rua do Sabiá', '110', 'Apto 101', 'Pedra', '11.400-121', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Lula Molusco', 'Rua da Bateria', '23', null, 'Moai', '54.212-121', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Sr. Siriguejo', 'Rua do Motorista', '123', 'Apto 302', 'Siri Cascudo', '38.400-12', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Sandy Bochechas', 'Av Rio Branco', '321', null, 'Cúpula', '56.400-121', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Plankton', 'Av Brasil', '100', null, 'Balde de Lixo', '77.400-121', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Karen', 'Rua do Sapo', '1120', 'Apto 201', 'Balde de Lixo', '12.400-121', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Pérola', 'Rua da Manga', '433', null, 'Siri Cascudo', '31.400-121', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Gary', 'Rua do Músico', '566', null, 'Abacaxi', '38.400-00', 'Fenda do Biquíni', 'OC', true);
INSERT INTO pessoa (nome, logradouro, numero, complemento, bairro, cep, cidade, estado, ativo) values ('Sra. Puff', 'Rua da Terra', '1233', 'Apto 10', 'Escola de Pilotagem', '99.400-121', 'Fenda do Biquíni', 'OC', true);
