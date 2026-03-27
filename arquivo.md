::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: container
::: header
# 📋 Análise do Método distribuir()

Fluxo Completo de Distribuição de Processos - DistribuicaoRN.php
:::

## [1]{.section-counter}Visão Geral do Método

O método `distribuir()`{.code-block} é o método principal da classe
`DistribuicaoRN` responsável por todo o fluxo de distribuição de
processos. Ele é um método complexo e robusto que envolve múltiplas
validações, processamentos e integrações com diversas tabelas do banco
de dados.

::: key-point
**📌 Ponto-Chave:** O método implementa toda a lógica de negócio para
distribuir um processo a um órgão judiciário, considerando competências,
prevenções, sorteios, dependências e múltiplas regras de negócio
específicas do tribunal.
:::

## [2]{.section-counter}FASE 1: Validações e Preparação

:::::::::::::::::::::::::: {.phase .validation}
### 2.1 - Verificações Iniciais

:::::::: step
::: step-number
1
:::

:::::: step-content
::: step-title
Bloqueio de Horários
:::

:::: step-description
Verifica se há restrição de horário para distribuição do processo

::: code-block
DistribuicaoRN::verificarBloqueioDistribuicaoProcesso();
:::
::::
::::::
::::::::

:::::::: step
::: step-number
2
:::

:::::: step-content
::: step-title
Coleta de Dados Básicos
:::

:::: step-description
Recupera informações fundamentais do processo (classe judicial, partes,
tipos de migração)

::: code-block
\$numIdClasseJudicial\
\$arrObjParteProcessoDTO\
\$bolMigracaoSiapro\
\$bolDigitalizacaoProcesso2G
:::
::::
::::::
::::::::

:::::::: step
::: step-number
3
:::

:::::: step-content
::: step-title
Busca de Dados da Classe Judicial
:::

:::: step-description
::: code-block
\$objClasseJudicialDTO =
\$this-\>buscarDadosDaClasse(\$numIdClasseJudicial);
:::
::::
::::::
::::::::

::::::: step
::: step-number
4
:::

::::: step-content
::: step-title
Validações Obrigatórias
:::

::: step-description
Executa um conjunto de validações críticas:

- **validarCondicoesDaClasse():** Verifica condições específicas da
  classe
- **validarDistribuicaoJusPostulandi():** Valida requisitos de Justiça
  Postulandi
- **validarDistribuicao():** Validação geral do processo para
  distribuição
:::
:::::
:::::::
::::::::::::::::::::::::::

## [3]{.section-counter}FASE 2: Determinação de Competência

::::::::::::::::::::::::::: {.phase .processing}
### 3.1 - Busca de Competências

:::::::: step
::: step-number
5
:::

:::::: step-content
::: step-title
Recuperar Juízos da Localidade
:::

:::: step-description
::: code-block
\$arrJuizos =
\$this-\>recuperarJuizosLocalidade(\$numIdLocalidadeJudicial,
\$arrJuizosUf);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
6
:::

:::::: step-content
::: step-title
Buscar Competências Disponíveis
:::

:::: step-description
Busca as competências associadas à classe e localidade

::: code-block
\$arrCompLocalidade =
\$this-\>buscarCompetenciasPorClasseLocalidade(\$numIdLocalidadeJudicial,
\$objClasseJudicialDTO);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
7
:::

:::::: step-content
::: step-title
(Não é verdade remover, método retorna apenas o cod do assunto
principal)Descobrir Competências com Assunto
:::

:::: step-description
Filtra apenas as competências que possuem o assunto principal do
processo

::: code-block
\$this-\>buscarCompetenciasComAssunto(\$numIdAssuntoPrincipal);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
8
:::

:::::: step-content
::: step-title
Selecionar Competência Final
:::

:::: step-description
Se não foi informada na tela de assuntos, utiliza a descoberta
automaticamente

::: code-block
\$numCodCompetencia
:::
::::
::::::
::::::::
:::::::::::::::::::::::::::

## [4]{.section-counter}FASE 3: Análise de Prevenção

:::::::::: {.phase .processing}
### 4.1 - Mecanismo de Prevenção

::: key-point
**🔍 Prevenção:** Verifica se o processo tem relacionamento com outro já
distribuído ao mesmo juízo, possibilitando distribuição por
\"prevenção\" em vez de sorteio
:::

:::::::: step
::: step-number
9
:::

:::::: step-content
::: step-title
Verificar Prevenção
:::

:::: step-description
::: code-block
\$this-\>verificarPrevencao( \$numProcessoOriginario,
\$numIdOrgaoJuizoOriginario, \$des_evento, \$bolDependencia,
\$arrRelacionados, \$arrPrevencaoTRF, \... );
:::

Outputs:

- **\$bolDependencia:** Indica distribuição por dependência (prevenção)
- **\$numIdOrgaoJuizoOriginario:** Órgão de origem identificado
- **\$des_evento:** Tipo de evento de distribuição
- **\$arrRelacionados:** Processos relacionados encontrados
::::
::::::
::::::::
::::::::::

## [5]{.section-counter}FASE 4: Regionalização (Opcional)

::::::::::::::: {.phase .processing}
:::::::: step
::: step-number
10
:::

:::::: step-content
::: step-title
Verificar Regionalização Habilitada
:::

:::: step-description
Alguns tribunais dividem a distribuição por regionalizações/áreas

::: code-block
\$bolRegionalizacaoDistribuicao =
ParametroEproc::getInstance()-\>getValor(\'EPROC_HABILITAR_REGIAO_DISTRIBUICAO\',false);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
11
:::

:::::: step-content
::: step-title
Buscar Órgãos da Região
:::

:::: step-description
Se regionalização está habilitada, busca órgãos da região específica

::: code-block
\$objOrgaoRegiaoDistribuicaoRN-\>getArrOrgaoRegiaoDistribuicao(\$arrParametros);
:::
::::
::::::
::::::::
:::::::::::::::

## [6]{.section-counter}FASE 5: Filtros Especiais de Órgãos

::::::::::::::::::::: {.phase .processing}
### 6.1 - Verificações de Elegibilidade

:::::::: step
::: step-number
12
:::

:::::: step-content
::: step-title
Juízos Exclusivos por Entidades
:::

:::: step-description
Algumas entidades (órgãos públicos, autarquias) possuem juízos
exclusivos

::: code-block
\$objJuizoExclusivoEntidadeRN-\>verificaJuizosExclusivosEntidades(\...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
13
:::

:::::: step-content
::: step-title
Conciliação
:::

:::: step-description
Verifica se classe deve ser distribuída para juízos de conciliação

::: code-block
\$bolConciliacao =
\$objClasseJudicialRN-\>deveSerDistribuidaParaJuizosDeConciliacao(\...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
14
:::

:::::: step-content
::: step-title
Afastamentos de Magistrados
:::

:::: step-description
Filtra magistrados que estão afastados (férias, licenças)

::: code-block
\$objMapaDistribuicaoAfastamento-\>verificarAfastamentos(\...);
:::
::::
::::::
::::::::
:::::::::::::::::::::

## [7]{.section-counter}FASE 6: Seleção do Órgão Judiciário

:::::::::::::::: {.phase .processing}
### 7.1 - Sorteio Equânime vs Prevenção

::: info
**ℹ️ Nota:** Se \$bolDependencia = true, o processo vai para o órgão de
origem. Caso contrário, é realizado sorteio entre os órgãos elegíveis.
:::

:::::::: step
::: step-number
15
:::

:::::: step-content
::: step-title
Buscar Juízos com Menor Quantidade
:::

:::: step-description
Identifica qual distribuição equânime utilizar (carga equilibrada)

::: code-block
\$arrNumIdMapaDistribuicao = \$this-\>buscarJuizosComMenorQuantidade(
\$objDistribuicaoEquanimeRN, \$arrJuizosCompetencia,
\$numCodCompetencia, \... );
:::
::::
::::::
::::::::

:::::::: step
::: step-number
16
:::

:::::: step-content
::: step-title
Realizar Sorteio (se aplicável)
:::

:::: step-description
Se não houver prevenção, realiza sorteio aleatório entre órgãos
elegíveis

::: code-block
\$this-\>sortear( \$numIdOrgao, \$numIdOrgaoJuizoOriginario,
\$arrNumIdMapaDistribuicao, \... );
:::
::::
::::::
::::::::
::::::::::::::::

## [8]{.section-counter}FASE 7: Geração do Número do Processo

::::::::::::::::::::: {.phase .processing}
:::::::: step
::: step-number
17
:::

:::::: step-content
::: step-title
Carregar Dados do Juízo
:::

:::: step-description
::: code-block
\$this-\>carregarDadosJuizo(\$numIdOrgao);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
18
:::

:::::: step-content
::: step-title
Recuperar Juízo e Secretaria
:::

:::: step-description
::: code-block
\$this-\>recuperarJuizoSecretaria(\$arrOrgaoDTO, \$numIdOrgao);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
19
:::

:::::: step-content
::: step-title
Gerar Número do Processo
:::

:::: step-description
Gera novo número de processo conforme padrão do CNJ ou específico do
tribunal

::: code-block
\$numNumProcesso = \$this-\>gerarNumeroProcesso(\...);
:::
::::
::::::
::::::::
:::::::::::::::::::::

## [9]{.section-counter}FASE 8: Salvamento em Banco de Dados

:::::::::::::::: {.phase .completion}
### 9.1 - Tabelas Principais Atualizadas

::: table-wrapper
  Tabela                                               Operação   Descrição
  ---------------------------------------------------- ---------- ----------------------------------------------
  [processo]{.database-table}                          INSERT     Insere registro principal do processo
  [controle_distribuicao]{.database-table}             UPDATE     Atualiza controle com número da distribuição
  [processo_evento]{.database-table}                   INSERT     Registra evento de distribuição
  [processo_fase_judicial]{.database-table}            INSERT     Registra fase processual
  [historico_distribuicao_judicial]{.database-table}   INSERT     Histórico completo da distribuição
  [processo_parte]{.database-table}                    INSERT     Partes envolvidas no processo
  [processo_assunto]{.database-table}                  INSERT     Assuntos principais e secundários
  [processo_dado_complementar]{.database-table}        INSERT     Dados complementares do processo
  [processo_relacionado]{.database-table}              INSERT     Processos relacionados/dependentes
  [mapa_distribuicao]{.database-table}                 UPDATE     Atualiza mapa de carga de trabalho
:::

:::::::: step
::: step-number
20
:::

:::::: step-content
::: step-title
Salvar Processo
:::

:::: step-description
::: code-block
\$this-\>salvarProcesso( \$numIdProcesso, \$numNumProcesso,
\$arrOrgaoDTO, \$objEventoJudicialDTO, \$numCodCompetencia, \... );
:::
::::
::::::
::::::::

:::::::: step
::: step-number
21
:::

:::::: step-content
::: step-title
Lançar Evento de Distribuição
:::

:::: step-description
::: code-block
\$numIdProcessoEvento = \$this-\>lancarEvento( \$numIdProcesso,
\$objEventoJudicialDTO, \$objFaseJudicialDTO, \... );
:::
::::
::::::
::::::::
::::::::::::::::

## [10]{.section-counter}FASE 9: Processamento de Documentos e Partes

:::::::::::::::::::::::::: {.phase .completion}
:::::::: step
::: step-number
22
:::

:::::: step-content
::: step-title
Gravar Documentos do Evento
:::

:::: step-description
::: code-block
\$arrIdDocumentos =
\$this-\>gravarProcessoEventoDocumento(\$numIdProcessoEvento);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
23
:::

:::::: step-content
::: step-title
Gravar Partes e Representações
:::

:::: step-description
::: code-block
\$this-\>gravarPartesRepresentacoesProcesso( \$arrIdProcessoParte,
\$arrDtoProcessoParte, \$numIdProcessoPartePF, \... );
:::
::::
::::::
::::::::

:::::::: step
::: step-number
24
:::

:::::: step-content
::: step-title
Vincular Procuradores às Partes
:::

:::: step-description
::: code-block
\$this-\>vincularProcuradorAParte( \$arrOutrosProcuradores,
\$arrIdProcessoParte, \... );
:::
::::
::::::
::::::::

::::::: step
::: step-number
25
:::

::::: step-content
::: step-title
Cadastrar Justiça Gratuita (se aplicável)
:::

::: step-description
Se a parte tem direito a justiça gratuita, registra no processo
:::
:::::
:::::::
::::::::::::::::::::::::::

## [11]{.section-counter}FASE 10: Dados Específicos por Tipo de Processo

::::::::::::::::::::::::::: {.phase .completion}
:::::::: step
::: step-number
26
:::

:::::: step-content
::: step-title
Dados Criminais
:::

:::: step-description
Se processo tem natureza criminal, copia/transfere dados criminais de
origem

::: code-block
\$this-\>copiarDadosCriminaisOrigem(\...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
27
:::

:::::: step-content
::: step-title
Dados de Execução Fiscal
:::

:::: step-description
Para CDA (Certidão de Dívida Ativa), cadastra número administrativo

::: code-block
\$this-\>cadastrarNumeroAdministrativoCda(\$numIdProcesso);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
28
:::

:::::: step-content
::: step-title
IPL (Inquérito Policial)
:::

:::: step-description
Se aplicável, cadastra informações de inquérito policial relacionado

::: code-block
\$this-\>cadastrarIpl(\$numIdProcesso, \$numIdProcessoEvento);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
29
:::

:::::: step-content
::: step-title
Bens Arrecadados
:::

:::: step-description
Registra BAP (Bem Apreendido Processo) se houver

::: code-block
\$this-\>cadastrarArrBapBemDTO(\$this-\>objProcessoDTO);
:::
::::
::::::
::::::::
:::::::::::::::::::::::::::

## [12]{.section-counter}FASE 11: Configuração de Localizadores

:::::::::::::::::::::: {.phase .completion}
::: info
**ℹ️ Localizadores:** São marcadores/etiquetas que identificam a
situação processual (ex: \"Custas Pendentes\", \"Distribuído\",
\"Sentença a Publicar\")
:::

:::::::: step
::: step-number
30
:::

:::::: step-content
::: step-title
Definir Localizador de Distribuição
:::

:::: step-description
::: code-block
\$this-\>definirLocalizador(\$objLocalizadorOrgaoDTO, \$arrOrgaoDTO,
\$bolDependencia);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
31
:::

:::::: step-content
::: step-title
Gravar Localizador do Processo
:::

:::: step-description
::: code-block
\$this-\>gravarLocalizadorProcesso(\$numIdProcesso,
\$objLocalizadorOrgaoDTO, \...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
32
:::

:::::: step-content
::: step-title
Localizador de Custas Pendentes
:::

:::: step-description
Se processo não tem justiça gratuita, marca custas como pendentes

::: code-block
ProcessoCustasAPI::CI_PENDENTE
:::
::::
::::::
::::::::
::::::::::::::::::::::

## [13]{.section-counter}FASE 12: Redistribuições Automáticas

::::::::::::::::::::: {.phase .completion}
### 13.1 - Órgãos Especiais de Segundo Nível

:::::::: step
::: step-number
33
:::

:::::: step-content
::: step-title
Juizados de Garantias
:::

:::: step-description
Para certos processos penais, há redistribuição automática aos Juizados
de Garantias

::: code-block
\$objJuizoGarantiasRN-\>verificaJuizoGarantias( \$numIdOrgaoTitular,
\$numIdClasseJudicial, \$numCodCompetencia );
:::
::::
::::::
::::::::

:::::::: step
::: step-number
34
:::

:::::: step-content
::: step-title
Órgãos de Auxílio
:::

:::: step-description
Verifica se processo deve ser redistribuído a órgãos de auxílio (varas
adjuntas)

::: code-block
\$objOrgaoAuxilioRN-\>verificaOrgaoAuxilio(\...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
35
:::

:::::: step-content
::: step-title
Equalização Regionalizada
:::

:::: step-description
Se regionalizacao ativa, pode redistribuir para equalizar carga entre
regiões

::: code-block
\$objOrgaoRegiaoDistribuicaoRN-\>verificarRedistribuicaoEqualizacao(\...);
:::
::::
::::::
::::::::
:::::::::::::::::::::

## [14]{.section-counter}FASE 13: Histórico e Registros

::::::::::::::::::::: {.phase .completion}
:::::::: step
::: step-number
36
:::

:::::: step-content
::: step-title
Gravar Histórico de Distribuição
:::

:::: step-description
Registra completo histórico com informações de órgão, competência,
data/hora

::: code-block
\$this-\>gravarHistoricoDistribuicao( \$objLocalidadeJudicialDTO,
\$numIdLocalidadeJudicial, \$dblIdDistribuicao, \$numIdProcesso, \... );
:::

**Tabela:** [historico_distribuicao_judicial]{.database-table}
::::
::::::
::::::::

:::::::: step
::: step-number
37
:::

:::::: step-content
::: step-title
Atualizar Mapa de Distribuição
:::

:::: step-description
Incrementa contador de processos distribuídos ao órgão

::: code-block
\$this-\>atualizarMapaDistribuicao(\$objDistribuicaoEquanimeRN,
\$numIdOrgao);
:::

**Tabela:** [mapa_distribuicao]{.database-table}
::::
::::::
::::::::

:::::::: step
::: step-number
38
:::

:::::: step-content
::: step-title
Log de Distribuição Regionalizada
:::

:::: step-description
Se aplicável, registra distribuição por região

::: code-block
\$this-\>atualizarControleDistribuicaoRegiao(\...);
:::

**Tabela:** [log_auxilio_regiao_processo]{.database-table}
::::
::::::
::::::::
:::::::::::::::::::::

## [15]{.section-counter}FASE 14: Notificações e Comunicações

::::::::::::::::::::: {.phase .completion}
:::::::: step
::: step-number
39
:::

:::::: step-content
::: step-title
Avisos para Partes
:::

:::: step-description
Gera notificação pendente para as partes do processo

::: code-block
\$objUsuarioAvisoComunicacaoRN-\>geraProcessoAvisoComunicacao(
\$numIdProcesso, \$arrNumIdProcessoParte, 3, \... );
:::

**Tabela:** [processo_aviso_comunicacao]{.database-table}
::::
::::::
::::::::

:::::::: step
::: step-number
40
:::

:::::: step-content
::: step-title
Email ao Distribuidor
:::

:::: step-description
Envia extrato da distribuição por email (se usuário é advogado ou
procurador)

::: code-block
\$objGeraEmailRN-\>enviaEmailEventoDistribuicao();
:::
::::
::::::
::::::::

:::::::: step
::: step-number
41
:::

:::::: step-content
::: step-title
Email Especial TRF
:::

:::: step-description
Para TRF4, gera email com informações de prevenção

::: code-block
\$this-\>gerarEmailDistribuicaoTRF(\...);
:::
::::
::::::
::::::::
:::::::::::::::::::::

## [16]{.section-counter}FASE 15: Finalizações e Sincronizações

::::::::::::::::::::::::::: {.phase .completion}
:::::::: step
::: step-number
42
:::

:::::: step-content
::: step-title
Executar Automatização de Localizadores
:::

:::: step-description
Se habilitado, executa regras programadas de mudança de localizadores

::: code-block
\$objControleLocalizadorSistemaRN-\>alteracaoLocalizadorProgramada(\...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
43
:::

:::::: step-content
::: step-title
Relacionar Processo na Origem (TRF)
:::

:::: step-description
Para TRF4, relaciona processo com os de origem e colegiados

::: code-block
\$this-\>relacionarProcessoNaOrigem(\...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
44
:::

:::::: step-content
::: step-title
Registrar Tempos de Etapas
:::

:::: step-description
Para análise de performance, grava tempos das etapas processadas

::: code-block
PeticaoInicialTempoRN::gravarTemposEtapasPeticaoInicial(\...);
:::
::::
::::::
::::::::

:::::::: step
::: step-number
45
:::

:::::: step-content
::: step-title
Limpar Cache
:::

:::: step-description
Remove processo do cache para evitar problemas de sincronização

::: code-block
CacheEproc::getInstance()-\>removerAtributo(\'AcessoUsuarioProcesso\_\'.\$numNumProcesso);
:::
::::
::::::
::::::::
:::::::::::::::::::::::::::

## [17]{.section-counter}Tipos de Eventos Gerados

::: table-wrapper
  Tipo de Evento                 Constante                    Situação
  ------------------------------ ---------------------------- --------------------------------------------------
  Distribuição por Sorteio       `DISTRIBUICAO_SORTEIO`       Processo distribuído aleatoriamente entre órgãos
  Distribuição por Prevenção     `DISTRIBUICAO_PREVENCAO`     Processo vai para órgão com processo relacionado
  Distribuição por Dependência   `DISTRIBUICAO_DEPENDENCIA`   Processo depende de outro já distribuído
  Redistribuição entre Seções    `REDISTRIBUICAO_SORTEIO`     Redistribuição entre seções (TRF)
  Remessa para TR                `REMESSA_A_TR`               Processo é remetido para Tribunal Regional
  Remetidos Autos                `REMETIDOS_AUTOS`            Autos remetidos para secretaria
:::

## [18]{.section-counter}Fluxo Geral do Método

::::::::::::::::::::::::::::::::::::: flow-diagram
::: {style="text-align: center; font-weight: bold; color: #667eea;"}
INÍCIO DO MÉTODO distribuir()
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Validações Iniciais
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Buscar Classe Judicial e Competências
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Verificar Prevenção (Relacionados)
:::

::: arrow
↓
:::

::: {style="background: #fff3cd; padding: 10px; border-radius: 4px; text-align: center;"}
**Sorteio ou Prevenção?**
:::

::::: {style="display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin: 10px 0;"}
::: {style="background: #c8e6c9; padding: 10px; border-radius: 4px; text-align: center;"}
SIM (Prevenção)\
→ Órgão de Origem
:::

::: {style="background: #ffccbc; padding: 10px; border-radius: 4px; text-align: center;"}
NÃO (Sorteio)\
→ Sortear Órgão
:::
:::::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Gerar Número do Processo
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Iniciar Transação do Banco
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Salvar Processo, Partes, Documentos
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Lançar Evento de Distribuição
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Configurar Localizadores
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Redistribuições Automáticas (Garantias, Auxílio)
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Gravar Histórico de Distribuição
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Atualizar Mapa de Distribuição
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Gerar Notificações e Emails
:::

::: arrow
↓
:::

::: {style="background: #e8f4f8; padding: 10px; border-radius: 4px; text-align: center;"}
Confirmar Transação do Banco
:::

::: arrow
↓
:::

::: {style="text-align: center; font-weight: bold; color: #667eea;"}
FIM DO MÉTODO distribuir()
:::
:::::::::::::::::::::::::::::::::::::

## [19]{.section-counter}Tratamento de Exceções

::: warning
**⚠️ Importante:** Todo o método está envolvido em try-catch que captura
exceções do tipo InfraException
:::

:::::::: step
::: step-number
A
:::

:::::: step-content
::: step-title
Se InfraException for capturada
:::

:::: step-description
- Formata mensagem de erro
- Desfaz transação do banco (rollback)
- Registra erro em log
- Retorna mensagem de erro formatada ao usuário

::: code-block
\$this-\>getMensagemErroDeDistribuicao(\$e, \...);
:::
::::
::::::
::::::::

## [20]{.section-counter}Pontos Críticos para Validação

:::: key-point
**1. Validação de Competência:** Processo sem competência válida com a
classe e localidade lança exceção

::: {.code-block style="margin-top: 10px;"}
if (numCodCompetencia == null) → InfraException
:::
::::

::: key-point
**2. Bloqueio de Horário:** Sistema pode estar em manutenção com
bloqueio de distribuição
:::

::: key-point
**3. Transação do Banco:** Se transação falha em qualquer ponto, tudo é
desfeito
:::

::: key-point
**4. Prevenção Correta:** Identificação de processos relacionados é
crítica para prevenção
:::

::: key-point
**5. Justiça Gratuita:** Incorreta marcação de justiça gratuita pode
impedir cobrança de custas
:::

::: key-point
**6. Partes e Procuradores:** Todas as partes e procuradores devem ser
salvas corretamente
:::

::: key-point
**7. Localizadores:** Localizadores iniciais devem ser coerentes com
tipo do processo
:::

::: key-point
**8. Dados Criminais (TRF):** Se aplicável, dados criminais devem ser
transferidos corretamente
:::

::: footer
📄 Análise gerada automáticamente \| Arquivo: DistribuicaoRN.php \|
Método: distribuir() \| Data: 2026

🔗 Banco de Dados: eproc1g \| Servidor: 10.27.145.177:3306
:::
:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
