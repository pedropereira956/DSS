CREATE DATABASE IF NOT EXISTS ChocapitosRestaurantes;
USE ChocapitosRestaurantes;

CREATE TABLE IF NOT EXISTS restaurantes (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    localizacao VARCHAR(255),
    capacidadeStock INT DEFAULT 0
);

CREATE TABLE IF NOT EXISTS funcionarios (
    id VARCHAR(50) PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    cargo VARCHAR(50),
    restaurante_id VARCHAR(50),
    FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS pedidos (
    id_interno INT AUTO_INCREMENT PRIMARY KEY,
    numero INT NOT NULL,
    restaurante_id VARCHAR(50),
    estado VARCHAR(50) DEFAULT 'PENDENTE',
    notas TEXT,
    metodo_pagamento VARCHAR(50),
    hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS itens_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT,
    produto_nome VARCHAR(100),
    quantidade INT,
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id_interno) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS stock (
    restaurante_id VARCHAR(50),
    item_nome VARCHAR(100),
    quantidade INT DEFAULT 0,
    PRIMARY KEY (restaurante_id, item_nome),
    FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS menus (
    nome VARCHAR(100) PRIMARY KEY,
    preco DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS menu_receitas (
    menu_nome VARCHAR(100),
    ingrediente_nome VARCHAR(100),
    quantidade INT DEFAULT 1,
    PRIMARY KEY (menu_nome, ingrediente_nome),
    FOREIGN KEY (menu_nome) REFERENCES menus(nome)
);

CREATE TABLE IF NOT EXISTS producao_tarefas (
    id_tarefa INT AUTO_INCREMENT PRIMARY KEY,
    pedido_id INT,
    item_nome VARCHAR(100),
    quantidade INT,
    posto_trabalho VARCHAR(50),
    prioridade INT DEFAULT 3,
    estado VARCHAR(20) DEFAULT 'PENDENTE',
    FOREIGN KEY (pedido_id) REFERENCES pedidos(id_interno) ON DELETE CASCADE
);